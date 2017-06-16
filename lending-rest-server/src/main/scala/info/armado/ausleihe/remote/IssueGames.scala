package info.armado.ausleihe.remote

import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.MediaType

import info.armado.ausleihe.database.access.{GamesDAO, IdentityCardDAO, LendGameDAO, LendIdentityCardDAO}
import info.armado.ausleihe.database.barcode._
import info.armado.ausleihe.database.entities.{IdentityCard, LendIdentityCard}
import info.armado.ausleihe.remote.dataobjects.inuse.{GameInUse, NotInUse}
import info.armado.ausleihe.remote.requests.IssueGameRequest
import info.armado.ausleihe.remote.results._
import info.armado.ausleihe.util.AutomaticListConvertable
import info.armado.ausleihe.util.DOExtensions._

@Path("/issue")
@RequestScoped
class IssueGames extends AutomaticListConvertable {
  @Inject var gamesDao: GamesDAO = _
  @Inject var identityCardDao: IdentityCardDAO = _
  @Inject var lendGameDao: LendGameDAO = _
  @Inject var lendIdentityCardDao: LendIdentityCardDAO = _

  def findIdentityCard(identityCardBarcode: Barcode): Option[Either[LendIdentityCard, IdentityCard]] = Option(lendIdentityCardDao.selectCurrentByIdentityCardBarcode(identityCardBarcode)) match {
    case Some(lendIdentityCard) => Some(Left(lendIdentityCard))
    case None => Option(identityCardDao.selectActivatedByBarcode(identityCardBarcode)) match {
      case Some(identityCard) => Some(Right(identityCard))
      case None => None
    }
  }

  @POST
  @Consumes(Array(MediaType.APPLICATION_XML))
  @Produces(Array(MediaType.APPLICATION_XML))
  @Path("/games")
  @Transactional
  def issueGames(issueGameRequest: IssueGameRequest): AbstractResult = issueGameRequest match {
    case IssueGameRequest(Some(identityCardBarcode), gameBarcodes, Some(limited)) => (ValidateBarcode(identityCardBarcode), gameBarcodes) match {
      case (ValidBarcode(identityCardBarcode), gameBarcodes @ Array(_, _*)) => gameBarcodes.find { ValidateBarcode(_).isInstanceOf[InvalidBarcode] } match {
        // at least one of the game barcodes is invalid
        case Some(invalidBarcode) => IncorrectBarcode(invalidBarcode)

        // all game barcodes are valid
        case None => {
          val identityCard: Option[Either[LendIdentityCard, IdentityCard]] = findIdentityCard(identityCardBarcode)

          val games = gameBarcodes.map { gameBarcode => gamesDao.selectActivatedByBarcode(new Barcode(gameBarcode)) }

          // a list of (Game, Barcode) pairs that are currently not issued/borrowed 
          val notActivatedGameBarcodePairs = games.map { Option(_) }.zip(gameBarcodes).filter { case (None, _) => true case (Some(_), _) => false }.toList

          // a list of all given games to be issued that are currently borrowed
          val alreadyBorrowedGames = games.map { game => lendGameDao.selectLendGameByGame(game) }.map { Option(_) }.flatten.toList

          (identityCard, notActivatedGameBarcodePairs, alreadyBorrowedGames) match {
            // the identity card has no lend games and all games are currently not borrowed
            case (Some(Left(lic @ LendIdentityCard(_, _, _, _, _, Nil))), Nil, Nil) => {
              lendGameDao.issueGames(games.toList, lic)
              IssueGamesSuccess(lic.toIdentityCardData, games.map { _.toGameData }.toArray)
            }

            // the lending mode is "unlimited" and none of the scanned games is currently borrowed
            case (Some(Left(lic @ LendIdentityCard(_, _, _, _, _, _))), Nil, Nil) if !limited => {
              lendGameDao.issueGames(games.toList, lic)
              IssueGamesSuccess(lic.toIdentityCardData, games.map { _.toGameData }.toArray)
            }

            // at least one of the given games is currently borrowed
            case (Some(Left(lendIdentityCard)), _, alreadyBorrowedGame :: _) => LendingEntityInUse(alreadyBorrowedGame.toGameData, GameInUse(alreadyBorrowedGame.toIdentityCardData, alreadyBorrowedGame.toEnvelopeData))

            // the identity card has currently at least one borrowed game
            case (Some(Left(lic @ LendIdentityCard(_, _, _, _, _, List(_, _*)))), _, _) if limited => IdentityCardHasIssuedGames(lic.toIdentityCardData, lic.toGameData)

            // the identity card is currently not issued              
            case (Some(Right(identityCard)), _, _) => LendingEntityInUse(identityCard.toIdentityCardData, NotInUse())

            // an identity card with the given barcode doesn't exists
            case (None, _, _) => LendingEntityNotExists(identityCardBarcode)

            // at least one of the game barcodes doesn't exist
            case (_, (None, gameBarcode) :: _, _) => LendingEntityNotExists(gameBarcode)
          }
        }
      }

      // the identity card barcode is invalid
      case (InvalidBarcode(identityCardBarcode), _) => IncorrectBarcode(identityCardBarcode)
    }

    // wrong input
    case _ => throw new BadRequestException()
  }

}