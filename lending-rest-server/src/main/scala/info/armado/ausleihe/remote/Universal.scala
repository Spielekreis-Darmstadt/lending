package info.armado.ausleihe.remote

import java.util.{List => JList}
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.MediaType

import info.armado.ausleihe.database.access._
import info.armado.ausleihe.database.barcode._
import info.armado.ausleihe.database.entities._
import info.armado.ausleihe.database.enums.Prefix
import info.armado.ausleihe.remote.dataobjects.entities.GameData
import info.armado.ausleihe.remote.dataobjects.information.GameInformation
import info.armado.ausleihe.remote.dataobjects.inuse.{EnvelopeInUse, GameInUse, IdentityCardInUse, NotInUse}
import info.armado.ausleihe.remote.requests.GameInformationRequest
import info.armado.ausleihe.remote.results._
import info.armado.ausleihe.util.AutomaticListConvertable
import info.armado.ausleihe.util.DOExtensions._

import scala.collection.JavaConverters._

@Path("/info")
@RequestScoped
class Universal extends AutomaticListConvertable {
  @Inject var gamesDao: GamesDAO = _
  @Inject var identityCardDao: IdentityCardDAO = _
  @Inject var envelopeDao: EnvelopeDAO = _
  @Inject var lendGameDao: LendGameDAO = _
  @Inject var lendIdentityCardDao: LendIdentityCardDAO = _

  @GET
  @Produces(Array(MediaType.APPLICATION_XML))
  @Path("/inuse/{barcode}")
  @Transactional
  def barcodeInUse(@PathParam("barcode") barcode: String): AbstractResult = ValidateBarcode(barcode) match {
    // The given barcode is gamelike
    case ValidBarcode(code @ Barcode(Prefix.BDKJ | Prefix.Spielekreis, counter, checksum)) => Option(gamesDao.selectActivatedByBarcode(code)) match {
      case Some(game) => Option(lendGameDao.selectLendGameByGame(game)) match {
        case Some(lendGame) => LendingEntityInUse(lendGame.toGameData, GameInUse(lendGame.toIdentityCardData, lendGame.toEnvelopeData))
        case None => LendingEntityInUse(game.toGameData, NotInUse())
      }
      case None => LendingEntityNotExists(barcode)
    }
    // The given barcode is idcardlike
    case ValidBarcode(code @ Barcode(Prefix.IdentityCards, counter, checksum)) => Option(identityCardDao.selectActivatedByBarcode(code)) match {
      case Some(idcard) => Option(lendIdentityCardDao.selectCurrentByIdentityCard(idcard)) match {
        case Some(lic @ LendIdentityCard(_, _, _, _, _, _)) => LendingEntityInUse(lic.toIdentityCardData, IdentityCardInUse(lic.toEnvelopeData, lic.toGameData))
        case None => LendingEntityInUse(idcard.toIdentityCardData, NotInUse())
      }
      case None => LendingEntityNotExists(barcode)
    }
    // The given barcode is envelopelike
    case ValidBarcode(code @ Barcode(Prefix.Envelopes, counter, checksum)) => Option(envelopeDao.selectActivatedByBarcode(code)) match {
      case Some(envelope) => Option(lendIdentityCardDao.selectCurrentByEnvelope(envelope)) match {
        case Some(lic @ LendIdentityCard(_, _, _, _, _, _)) => LendingEntityInUse(lic.toEnvelopeData, EnvelopeInUse(lic.toIdentityCardData, lic.toGameData))
        case None => LendingEntityInUse(envelope.toEnvelopeData, NotInUse())
      }
      case None => LendingEntityNotExists(barcode)
    }
    // The given barcode is not valid and therefore an incorrect barcode
    case InvalidBarcode(barcode) => IncorrectBarcode(barcode)
  }

  @POST
  @Consumes(Array(MediaType.APPLICATION_XML))
  @Produces(Array(MediaType.APPLICATION_XML))
  @Path("/gamesinformation")
  def gamesInformation(gameInformationRequest: GameInformationRequest): GameInformation = gameInformationRequest match {
    case request @ GameInformationRequest(_, _, _, _, _, _, _) => {
      val foundGames = gamesDao.selectAllGamesWithRequirements(request.searchTerm, request.searchTitle, request.searchAuthor, request.searchPublisher, request.playerCount, request.minimumAge, request.gameDuration).asScala
        .filter { _.getAvailable }
        .map { game => game.toLendGameStatusData(lendGameDao.selectLendGameByGame(game)) }.toArray

      GameInformation(gameInformationRequest, foundGames)
    }
    case _ => throw new BadRequestException()
  }

  @GET
  @Produces(Array(MediaType.APPLICATION_XML))
  @Path("/statusinformation/{barcode}")
  @Transactional
  def statusInformation(@PathParam("barcode") barcode: String): AbstractResult = ValidateBarcode(barcode) match {
    // The given barcode is gamelike
    case ValidBarcode(code @ Barcode(Prefix.BDKJ | Prefix.Spielekreis, counter, checksum)) => Option(gamesDao.selectActivatedByBarcode(code)) match {
      case None => LendingEntityNotExists(barcode)
      case Some(game) => Option(lendGameDao.selectLendGameByGame(game)) match {
        case Some(lendGame @ LendGame(_, _, _, _)) => Information(Array(lendGame.toGameData), lendGame.toIdentityCardData, lendGame.toEnvelopeData)
        case None => Information(Array(game.toGameData), null, null)
      }
    }
    // The given barcode is idcardlike
    case ValidBarcode(code @ Barcode(Prefix.IdentityCards, counter, checksum)) => Option(identityCardDao.selectActivatedByBarcode(code)) match {
      case None => LendingEntityNotExists(barcode)
      case Some(idcard) => Option(lendIdentityCardDao.selectCurrentByIdentityCard(idcard)) match {
        case Some(lic @ LendIdentityCard(_, _, _, _, _, _)) => Information(lic.toGameData, lic.toIdentityCardData, lic.toEnvelopeData)
        case None => Information(Array.empty[GameData], idcard.toIdentityCardData, null)
      }
    }
    // The given barcode is envelopelike
    case ValidBarcode(code @ Barcode(Prefix.Envelopes, counter, checksum)) => Option(envelopeDao.selectActivatedByBarcode(code)) match {
      case None => LendingEntityNotExists(barcode)
      case Some(envelope) => Option(lendIdentityCardDao.selectCurrentByEnvelope(envelope)) match {
        case Some(lic @ LendIdentityCard(_, _, _, _, _, _)) => Information(lic.toGameData, lic.toIdentityCardData, lic.toEnvelopeData)
        case None => Information(Array.empty[GameData], null, envelope.toEnvelopeData)
      }
    }
    case InvalidBarcode(barcode) => IncorrectBarcode(barcode)
  }
}