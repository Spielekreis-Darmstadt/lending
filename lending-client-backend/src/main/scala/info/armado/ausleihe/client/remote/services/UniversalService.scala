package info.armado.ausleihe.client.remote.services

import info.armado.ausleihe.client.transport.dataobjects.entities._
import info.armado.ausleihe.client.transport.dataobjects.information.GameInformationDTO
import info.armado.ausleihe.client.transport.dataobjects.inuse._
import info.armado.ausleihe.client.transport.requests.GameInformationRequestDTO
import info.armado.ausleihe.client.transport.results._
import info.armado.ausleihe.client.util.AutomaticListConvertable
import info.armado.ausleihe.client.util.DTOExtensions._
import info.armado.ausleihe.database.access._
import info.armado.ausleihe.database.barcode._
import info.armado.ausleihe.database.dataobjects.Prefix
import info.armado.ausleihe.database.entities._
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.MediaType

@Path("/info")
@RequestScoped
class UniversalService extends AutomaticListConvertable {
  @Inject var gamesDao: GamesDao = _
  @Inject var identityCardDao: IdentityCardDao = _
  @Inject var envelopeDao: EnvelopeDao = _
  @Inject var lendGameDao: LendGameDao = _
  @Inject var lendIdentityCardDao: LendIdentityCardDao = _

  @GET
  @Produces(Array(MediaType.APPLICATION_XML))
  @Path("/inuse/{barcode}")
  @Transactional
  def barcodeInUse(@PathParam("barcode") barcode: String): AbstractResultDTO =
    ValidateBarcode(barcode) match {
      // The given barcode is gamelike
      case ValidBarcode(code @ Barcode(Prefix.BDKJ | Prefix.Spielekreis, counter, checksum)) =>
        gamesDao.selectActivatedByBarcode(code) match {
          case Some(game) =>
            lendGameDao.selectLendGameByGame(game) match {
              case Some(lendGame) =>
                LendingEntityInUseDTO(
                  lendGame.toGameDTO,
                  GameInUseDTO(lendGame.toIdentityCardDTO, lendGame.toEnvelopeDTO)
                )
              case None => LendingEntityInUseDTO(game.toGameDTO, NotInUseDTO())
            }
          case None => LendingEntityNotExistsDTO(barcode)
        }
      // The given barcode is idcardlike
      case ValidBarcode(code @ Barcode(Prefix.IdentityCards, counter, checksum)) =>
        identityCardDao.selectActivatedByBarcode(code) match {
          case Some(idcard) =>
            lendIdentityCardDao.selectCurrentByIdentityCard(idcard) match {
              case Some(lic: LendIdentityCard) =>
                LendingEntityInUseDTO(
                  lic.toIdentityCardDTO,
                  IdentityCardInUseDTO(lic.toEnvelopeDTO, lic.toGameDTO)
                )
              case None => LendingEntityInUseDTO(idcard.toIdentityCardDTO, NotInUseDTO())
            }
          case None => LendingEntityNotExistsDTO(barcode)
        }
      // The given barcode is envelopelike
      case ValidBarcode(code @ Barcode(Prefix.Envelopes, counter, checksum)) =>
        envelopeDao.selectActivatedByBarcode(code) match {
          case Some(envelope) =>
            lendIdentityCardDao.selectCurrentByEnvelope(envelope) match {
              case Some(lic: LendIdentityCard) =>
                LendingEntityInUseDTO(
                  lic.toEnvelopeDTO,
                  EnvelopeInUseDTO(lic.toIdentityCardDTO, lic.toGameDTO)
                )
              case None => LendingEntityInUseDTO(envelope.toEnvelopeDTO, NotInUseDTO())
            }
          case None => LendingEntityNotExistsDTO(barcode)
        }
      // The given barcode is not valid and therefore an incorrect barcode
      case InvalidBarcode(_) => IncorrectBarcodeDTO(barcode)
    }

  @POST
  @Consumes(Array(MediaType.APPLICATION_XML))
  @Produces(Array(MediaType.APPLICATION_XML))
  @Path("/gamesinformation")
  def gamesInformation(gameInformationRequest: GameInformationRequestDTO): GameInformationDTO =
    gameInformationRequest match {
      case request: GameInformationRequestDTO => {
        val foundGames = gamesDao
          .selectAllGamesWithRequirements(
            Option(request.searchTerm),
            Option(request.searchTitle),
            Option(request.searchAuthor),
            Option(request.searchPublisher),
            Option(request.playerCount),
            Option(request.minimumAge),
            Option(request.gameDuration),
            Option(request.releaseYear)
          )
          .filter(_.available)
          .map(game => game.toLendGameStatusDTO(lendGameDao.selectLendGameByGame(game)))
          .toArray

        GameInformationDTO(gameInformationRequest, foundGames)
      }

      case _ => throw new BadRequestException()
    }

  @GET
  @Produces(Array(MediaType.APPLICATION_XML))
  @Path("/statusinformation/{barcode}")
  @Transactional
  def statusInformation(@PathParam("barcode") barcode: String): AbstractResultDTO =
    ValidateBarcode(barcode) match {
      // The given barcode is gamelike
      case ValidBarcode(code @ Barcode(Prefix.BDKJ | Prefix.Spielekreis, counter, checksum)) =>
        gamesDao.selectActivatedByBarcode(code) match {
          case None => LendingEntityNotExistsDTO(barcode)
          case Some(game) =>
            lendGameDao.selectLendGameByGame(game) match {
              case Some(lendGame: LendGame) =>
                InformationDTO(
                  Array(lendGame.toGameDTO),
                  lendGame.toIdentityCardDTO,
                  lendGame.toEnvelopeDTO
                )
              case None => InformationDTO(Array(game.toGameDTO), null, null)
            }
        }
      // The given barcode is idcardlike
      case ValidBarcode(code @ Barcode(Prefix.IdentityCards, counter, checksum)) =>
        identityCardDao.selectActivatedByBarcode(code) match {
          case None => LendingEntityNotExistsDTO(barcode)
          case Some(idcard) =>
            lendIdentityCardDao.selectCurrentByIdentityCard(idcard) match {
              case Some(lic: LendIdentityCard) =>
                InformationDTO(lic.toGameDTO, lic.toIdentityCardDTO, lic.toEnvelopeDTO)
              case None => InformationDTO(Array.empty[GameDTO], idcard.toIdentityCardDTO, null)
            }
        }
      // The given barcode is envelopelike
      case ValidBarcode(code @ Barcode(Prefix.Envelopes, counter, checksum)) =>
        envelopeDao.selectActivatedByBarcode(code) match {
          case None => LendingEntityNotExistsDTO(barcode)
          case Some(envelope) =>
            lendIdentityCardDao.selectCurrentByEnvelope(envelope) match {
              case Some(lic: LendIdentityCard) =>
                InformationDTO(lic.toGameDTO, lic.toIdentityCardDTO, lic.toEnvelopeDTO)
              case None => InformationDTO(Array.empty[GameDTO], null, envelope.toEnvelopeDTO)
            }
        }
      case InvalidBarcode(_) => IncorrectBarcodeDTO(barcode)
    }
}
