package info.armado.ausleihe.remote

import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.MediaType

import info.armado.ausleihe.database.access._
import info.armado.ausleihe.database.barcode._
import info.armado.ausleihe.database.dataobjects.Prefix
import info.armado.ausleihe.database.entities._
import info.armado.ausleihe.remote.dataobjects.entities.GameData
import info.armado.ausleihe.remote.dataobjects.information.GameInformation
import info.armado.ausleihe.remote.dataobjects.inuse.{EnvelopeInUse, GameInUse, IdentityCardInUse, NotInUse}
import info.armado.ausleihe.remote.requests.GameInformationRequest
import info.armado.ausleihe.remote.results._
import info.armado.ausleihe.util.AutomaticListConvertable
import info.armado.ausleihe.util.DOExtensions._

@Path("/info")
@RequestScoped
class Universal extends AutomaticListConvertable {
  @Inject var gamesDao: GamesDao = _
  @Inject var identityCardDao: IdentityCardDao = _
  @Inject var envelopeDao: EnvelopeDao = _
  @Inject var lendGameDao: LendGameDao = _
  @Inject var lendIdentityCardDao: LendIdentityCardDao = _

  @GET
  @Produces(Array(MediaType.APPLICATION_XML))
  @Path("/inuse/{barcode}")
  @Transactional
  def barcodeInUse(@PathParam("barcode") barcode: String): AbstractResult = ValidateBarcode(barcode) match {
    // The given barcode is gamelike
    case ValidBarcode(code @ Barcode(Prefix.BDKJ | Prefix.Spielekreis, counter, checksum)) => gamesDao.selectActivatedByBarcode(code) match {
      case Some(game) => lendGameDao.selectLendGameByGame(game) match {
        case Some(lendGame) => LendingEntityInUse(lendGame.toGameData, GameInUse(lendGame.toIdentityCardData, lendGame.toEnvelopeData))
        case None => LendingEntityInUse(game.toGameData, NotInUse())
      }
      case None => LendingEntityNotExists(barcode)
    }
    // The given barcode is idcardlike
    case ValidBarcode(code @ Barcode(Prefix.IdentityCards, counter, checksum)) => identityCardDao.selectActivatedByBarcode(code) match {
      case Some(idcard) => lendIdentityCardDao.selectCurrentByIdentityCard(idcard) match {
        case Some(lic: LendIdentityCard) => LendingEntityInUse(lic.toIdentityCardData, IdentityCardInUse(lic.toEnvelopeData, lic.toGameData))
        case None => LendingEntityInUse(idcard.toIdentityCardData, NotInUse())
      }
      case None => LendingEntityNotExists(barcode)
    }
    // The given barcode is envelopelike
    case ValidBarcode(code @ Barcode(Prefix.Envelopes, counter, checksum)) => envelopeDao.selectActivatedByBarcode(code) match {
      case Some(envelope) => lendIdentityCardDao.selectCurrentByEnvelope(envelope) match {
        case Some(lic: LendIdentityCard) => LendingEntityInUse(lic.toEnvelopeData, EnvelopeInUse(lic.toIdentityCardData, lic.toGameData))
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
      val foundGames = gamesDao.selectAllGamesWithRequirements(
        Option(request.searchTerm), Option(request.searchTitle), Option(request.searchAuthor),
        Option(request.searchPublisher), Option(request.playerCount), Option(request.minimumAge),
        Option(request.gameDuration))
        .filter { _.available }
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
    case ValidBarcode(code @ Barcode(Prefix.BDKJ | Prefix.Spielekreis, counter, checksum)) => gamesDao.selectActivatedByBarcode(code) match {
      case None => LendingEntityNotExists(barcode)
      case Some(game) => lendGameDao.selectLendGameByGame(game) match {
        case Some(lendGame: LendGame) => Information(Array(lendGame.toGameData), lendGame.toIdentityCardData, lendGame.toEnvelopeData)
        case None => Information(Array(game.toGameData), null, null)
      }
    }
    // The given barcode is idcardlike
    case ValidBarcode(code @ Barcode(Prefix.IdentityCards, counter, checksum)) => identityCardDao.selectActivatedByBarcode(code) match {
      case None => LendingEntityNotExists(barcode)
      case Some(idcard) => lendIdentityCardDao.selectCurrentByIdentityCard(idcard) match {
        case Some(lic: LendIdentityCard) => Information(lic.toGameData, lic.toIdentityCardData, lic.toEnvelopeData)
        case None => Information(Array.empty[GameData], idcard.toIdentityCardData, null)
      }
    }
    // The given barcode is envelopelike
    case ValidBarcode(code @ Barcode(Prefix.Envelopes, counter, checksum)) => envelopeDao.selectActivatedByBarcode(code) match {
      case None => LendingEntityNotExists(barcode)
      case Some(envelope) => lendIdentityCardDao.selectCurrentByEnvelope(envelope) match {
        case Some(lic: LendIdentityCard) => Information(lic.toGameData, lic.toIdentityCardData, lic.toEnvelopeData)
        case None => Information(Array.empty[GameData], null, envelope.toEnvelopeData)
      }
    }
    case InvalidBarcode(barcode) => IncorrectBarcode(barcode)
  }
}