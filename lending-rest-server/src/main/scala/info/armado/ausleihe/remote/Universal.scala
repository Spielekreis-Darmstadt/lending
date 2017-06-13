package info.armado.ausleihe.remote

import javax.ws.rs.Consumes
import info.armado.ausleihe.database.access.LendGameDAO
import info.armado.ausleihe.database.access.LendIdentityCardDAO
import javax.transaction.Transactional
import info.armado.ausleihe.remote.dataobjects.entities.GameData
import info.armado.ausleihe.database.dataobjects.ValidBarcode
import info.armado.ausleihe.remote.results.AbstractResult
import info.armado.ausleihe.database.access.GamesDAO
import javax.inject.Inject
import info.armado.ausleihe.database.access.EnvelopeDAO
import info.armado.ausleihe.database.access.IdentityCardDAO
import info.armado.ausleihe.remote.results.Information
import info.armado.ausleihe.database.enums.Prefix
import javax.ws.rs.GET
import info.armado.ausleihe.remote.results.LendingEntityNotExists
import info.armado.ausleihe.remote.results.IncorrectBarcode
import info.armado.ausleihe.remote.results.LendingEntityInUse
import javax.ws.rs.Produces
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.enterprise.context.RequestScoped
import info.armado.ausleihe.database.dataobjects.Barcode
import info.armado.ausleihe.remote.dataobjects.LendGameStatusData
import javax.ws.rs.core.MediaType
import info.armado.ausleihe.util.DOExtensions._
import scala.collection.JavaConverters._
import java.util.{ List => JList }
import info.armado.ausleihe.remote.dataobjects.inuse.NotInUse
import info.armado.ausleihe.remote.dataobjects.inuse.GameInUse
import info.armado.ausleihe.remote.dataobjects.inuse.IdentityCardInUse
import info.armado.ausleihe.remote.dataobjects.inuse.EnvelopeInUse
import info.armado.ausleihe.database.dataobjects.ValidateBarcode
import info.armado.ausleihe.database.dataobjects.InvalidBarcode
import info.armado.ausleihe.database.dataobjects.LendGame
import info.armado.ausleihe.database.dataobjects.LendIdentityCard
import info.armado.ausleihe.remote.dataobjects.inuse.NotInUse
import info.armado.ausleihe.util.AutomaticListConvertable
import javax.ws.rs.POST
import info.armado.ausleihe.remote.dataobjects.information.GameInformation
import info.armado.ausleihe.remote.requests.GameInformationRequest
import javax.ws.rs.core.Response
import javax.ws.rs.BadRequestException

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