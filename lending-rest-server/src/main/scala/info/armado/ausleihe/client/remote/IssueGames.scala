package info.armado.ausleihe.client.remote

import info.armado.ausleihe.client.util.AutomaticListConvertable
import info.armado.ausleihe.client.util.DTOExtensions._
import info.armado.ausleihe.database.access._
import info.armado.ausleihe.database.barcode._
import info.armado.ausleihe.database.entities.{Game, IdentityCard, LendGame, LendIdentityCard}
import info.armado.ausleihe.client.transport.dataobjects.inuse.{GameInUseDTO, NotInUseDTO}
import info.armado.ausleihe.client.transport.requests.IssueGameRequestDTO
import info.armado.ausleihe.client.transport.results._
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.MediaType

@Path("/issue")
@RequestScoped
class IssueGames extends AutomaticListConvertable {
  @Inject var gamesDao: GamesDao = _
  @Inject var identityCardDao: IdentityCardDao = _
  @Inject var lendGameDao: LendGameDao = _
  @Inject var lendIdentityCardDao: LendIdentityCardDao = _

  def findIdentityCard(identityCardBarcode: Barcode): Option[Either[LendIdentityCard, IdentityCard]] =
    lendIdentityCardDao.selectCurrentByIdentityCardBarcode(identityCardBarcode) match {
      case Some(lendIdentityCard) => Some(Left(lendIdentityCard))
      case None => identityCardDao.selectActivatedByBarcode(identityCardBarcode) match {
        case Some(identityCard) => Some(Right(identityCard))
        case None => None
      }
    }

  @POST
  @Consumes(Array(MediaType.APPLICATION_XML))
  @Produces(Array(MediaType.APPLICATION_XML))
  @Path("/games")
  @Transactional
  def issueGames(issueGameRequest: IssueGameRequestDTO): AbstractResultDTO = issueGameRequest match {
    case IssueGameRequestDTO(Some(identityCardBarcode), gameBarcodes, Some(limited)) => (ValidateBarcode(identityCardBarcode), gameBarcodes) match {
      case (ValidBarcode(identityCardBarcode), gameBarcodes@Array(_, _*)) => gameBarcodes.find {
        ValidateBarcode(_).isInstanceOf[InvalidBarcode]
      } match {
        // at least one of the game barcodes is invalid
        case Some(invalidBarcode) => IncorrectBarcodeDTO(invalidBarcode)

        // all game barcodes are valid
        case None => {
          val identityCard: Option[Either[LendIdentityCard, IdentityCard]] = findIdentityCard(identityCardBarcode)

          val games: List[Option[Game]] =
            gameBarcodes.map { gameBarcode => gamesDao.selectActivatedByBarcode(new Barcode(gameBarcode)) }.toList

          // a list of (Game, Barcode) pairs that are currently not issued/borrowed 
          val notActivatedGameBarcodePairs: List[(Option[Game], String)] =
            games.zip(gameBarcodes).filter { case (None, _) => true case (Some(_), _) => false }

          // a list of all given games to be issued that are currently borrowed
          val alreadyBorrowedGames: List[LendGame] = games.flatMap {
            case Some(game) => lendGameDao.selectLendGameByGame(game)
            case None => None
          }

          (identityCard, notActivatedGameBarcodePairs, alreadyBorrowedGames) match {
            // the identity card has no lend games and all games are currently not borrowed
            case (Some(Left(lic@LendIdentityCard(_, _, _, _, _))), Nil, Nil) if lic.hasNoCurrentLendGames => {
              lendGameDao.issueGames(games.flatten, lic)
              IssueGamesSuccessDTO(lic.toIdentityCardDTO, games.flatten.map {
                _.toGameDTO
              }.toArray)
            }

            // the lending mode is "unlimited" and none of the scanned games is currently borrowed
            case (Some(Left(lic@LendIdentityCard(_, _, _, _, _))), Nil, Nil) if !limited => {
              lendGameDao.issueGames(games.flatten, lic)
              IssueGamesSuccessDTO(lic.toIdentityCardDTO, games.flatten.map {
                _.toGameDTO
              }.toArray)
            }

            // at least one of the given games is currently borrowed
            case (Some(Left(lendIdentityCard)), _, alreadyBorrowedGame :: _) => LendingEntityInUseDTO(alreadyBorrowedGame.toGameDTO, GameInUseDTO(alreadyBorrowedGame.toIdentityCardDTO, alreadyBorrowedGame.toEnvelopeDTO))

            // the identity card has currently at least one borrowed game
            case (Some(Left(lic@LendIdentityCard(_, _, _, _, _))), _, _) if limited && lic.hasCurrentLendGames => IdentityCardHasIssuedGamesDTO(lic.toIdentityCardDTO, lic.toGameDTO)

            // the identity card is currently not issued              
            case (Some(Right(identityCard)), _, _) => LendingEntityInUseDTO(identityCard.toIdentityCardDTO, NotInUseDTO())

            // an identity card with the given barcode doesn't exists
            case (None, _, _) => LendingEntityNotExistsDTO(identityCardBarcode.toString)

            // at least one of the game barcodes doesn't exist
            case (_, (None, gameBarcode) :: _, _) => LendingEntityNotExistsDTO(gameBarcode)
          }
        }
      }

      // the identity card barcode is invalid
      case (InvalidBarcode(identityCardBarcode), _) => IncorrectBarcodeDTO(identityCardBarcode)
    }

    // wrong input
    case _ => throw new BadRequestException()
  }

}