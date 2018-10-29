package info.armado.ausleihe.client.remote.services

import info.armado.ausleihe.client.transport.dataobjects.inuse.NotInUseDTO
import info.armado.ausleihe.client.transport.requests.ReturnGameRequestDTO
import info.armado.ausleihe.client.transport.results.{AbstractResultDTO, IncorrectBarcodeDTO, ReturnGameSuccessDTO, _}
import info.armado.ausleihe.client.util.DTOExtensions._
import info.armado.ausleihe.database.access.{GamesDao, LendGameDao}
import info.armado.ausleihe.database.barcode._
import info.armado.ausleihe.database.entities.{Game, LendGame}
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.MediaType

@Path("/return")
@RequestScoped
class ReturnGamesService {
  @Inject var gamesDao: GamesDao = _
  @Inject var lendGameDao: LendGameDao = _

  def findGame(gameBarcode: Barcode): Option[Either[LendGame, Game]] =
    lendGameDao.selectLendGameByGameBarcode(gameBarcode) match {
      case Some(lendGame) => Some(Left(lendGame))
      case None => gamesDao.selectActivatedByBarcode(gameBarcode) match {
        case Some(game) => Some(Right(game))
        case None => None
      }
    }

  @POST
  @Consumes(Array(MediaType.APPLICATION_XML))
  @Produces(Array(MediaType.APPLICATION_XML))
  @Path("/games")
  @Transactional
  def returnGame(returnGameRequest: ReturnGameRequestDTO): AbstractResultDTO = returnGameRequest match {
    case ReturnGameRequestDTO(Some(gameBarcode)) => ValidateBarcode(gameBarcode) match {
      // the given barcode is a valid barcode
      case ValidBarcode(gameBarcode) => findGame(gameBarcode) match {
        // the game is currently borrowed by someone
        case Some(Left(lendGame)) => {
          lendGameDao.returnGame(lendGame)
          ReturnGameSuccessDTO(lendGame.toGameDTO)
        }

        // the game is currently not borrowed
        case Some(Right(game)) => LendingEntityInUseDTO(game.toGameDTO, NotInUseDTO())

        // a game with the given barcode doesn't exist
        case None => LendingEntityNotExistsDTO(gameBarcode.toString)
      }

      case InvalidBarcode(_) => IncorrectBarcodeDTO(gameBarcode)
    }

    // wrong input
    case _ => throw new BadRequestException()
  }
}