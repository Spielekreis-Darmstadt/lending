package info.armado.ausleihe.remote

import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.MediaType

import info.armado.ausleihe.database.access.{GamesDAO, LendGameDAO}
import info.armado.ausleihe.database.barcode._
import info.armado.ausleihe.database.entities.{Game, LendGame}
import info.armado.ausleihe.remote.dataobjects.inuse.NotInUse
import info.armado.ausleihe.remote.requests.ReturnGameRequest
import info.armado.ausleihe.remote.results._
import info.armado.ausleihe.util.DOExtensions.{GameExtension, LendGameExtension, barcodeToString}

@Path("/return")
@RequestScoped
class ReturnGames {
  @Inject var gamesDao: GamesDAO = _
  @Inject var lendGameDao: LendGameDAO = _

  def findGame(gameBarcode: Barcode): Option[Either[LendGame, Game]] = Option(lendGameDao.selectLendGameByGameBarcode(gameBarcode)) match {
    case Some(lendGame) => Some(Left(lendGame))
    case None => Option(gamesDao.selectActivatedByBarcode(gameBarcode)) match {
      case Some(game) => Some(Right(game))
      case None => None
    }
  }

  @POST
  @Consumes(Array(MediaType.APPLICATION_XML))
  @Produces(Array(MediaType.APPLICATION_XML))
  @Path("/games")
  @Transactional
  def returnGame(returnGameRequest: ReturnGameRequest): AbstractResult = returnGameRequest match {
    case ReturnGameRequest(Some(gameBarcode)) => ValidateBarcode(gameBarcode) match {
      // the given barcode is a valid barcode
      case ValidBarcode(gameBarcode) => findGame(gameBarcode) match {
        // the game is currently borrowed by someone
        case Some(Left(lendGame)) => {
          lendGameDao.returnGame(lendGame)
          ReturnGameSuccess(lendGame.toGameData)
        }

        // the game is currently not borrowed
        case Some(Right(game)) => LendingEntityInUse(game.toGameData, NotInUse())

        // a game with the given barcode doesn't exist
        case None => LendingEntityNotExists(gameBarcode)
      }

      case InvalidBarcode(gameBarcode) => IncorrectBarcode(gameBarcode)
    }

    // wrong input
    case _ => throw new BadRequestException()
  }
}