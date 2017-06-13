package info.armado.ausleihe.remote

import scala.Left
import scala.Right

import info.armado.ausleihe.database.access.GamesDAO
import info.armado.ausleihe.database.access.LendGameDAO
import info.armado.ausleihe.database.dataobjects.Barcode
import info.armado.ausleihe.database.dataobjects.Game
import info.armado.ausleihe.database.dataobjects.InvalidBarcode
import info.armado.ausleihe.database.dataobjects.LendGame
import info.armado.ausleihe.database.dataobjects.ValidBarcode
import info.armado.ausleihe.database.dataobjects.ValidateBarcode
import info.armado.ausleihe.util.DOExtensions.GameExtension
import info.armado.ausleihe.util.DOExtensions.LendGameExtension
import info.armado.ausleihe.util.DOExtensions.barcodeToString
import info.armado.ausleihe.remote.dataobjects.inuse.NotInUse
import info.armado.ausleihe.remote.requests.ReturnGameRequest
import info.armado.ausleihe.remote.results.AbstractResult
import info.armado.ausleihe.remote.results.IdentityCardEnvelopeNotBound
import info.armado.ausleihe.remote.results.IdentityCardHasIssuedGames
import info.armado.ausleihe.remote.results.IncorrectBarcode
import info.armado.ausleihe.remote.results.Information
import info.armado.ausleihe.remote.results.IssueGamesSuccess
import info.armado.ausleihe.remote.results.IssueIdentityCardSuccess
import info.armado.ausleihe.remote.results.LendingEntityInUse
import info.armado.ausleihe.remote.results.LendingEntityNotExists
import info.armado.ausleihe.remote.results.ReturnGameSuccess
import info.armado.ausleihe.remote.results.ReturnIdentityCardSuccess
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.BadRequestException
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlSeeAlso
import javax.ws.rs.core.MediaType

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