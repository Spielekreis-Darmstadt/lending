package info.armado.ausleihe.client.remote.services

import info.armado.ausleihe.client.transport.dataobjects.inuse.NotInUseDTO
import info.armado.ausleihe.client.transport.requests.ReturnIdentityCardRequestDTO
import info.armado.ausleihe.client.transport.results.{AbstractResultDTO, IdentityCardEnvelopeNotBoundDTO, IncorrectBarcodeDTO, _}
import info.armado.ausleihe.client.util.DTOExtensions._
import info.armado.ausleihe.database.access._
import info.armado.ausleihe.database.barcode._
import info.armado.ausleihe.database.entities.{Envelope, IdentityCard, LendIdentityCard}
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.MediaType

@Path("/return")
@RequestScoped
class ReturnIdentityCardsService {
  @Inject var identityCardDao: IdentityCardDao = _
  @Inject var envelopeDao: EnvelopeDao = _
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

  def findEnvelope(envelopeBarcode: Barcode): Option[Either[LendIdentityCard, Envelope]] =
    lendIdentityCardDao.selectCurrentByEnvelopeBarcode(envelopeBarcode) match {
      case Some(lendIdentityCard) => Some(Left(lendIdentityCard))
      case None => envelopeDao.selectActivatedByBarcode(envelopeBarcode) match {
        case Some(envelope) => Some(Right(envelope))
        case None => None
      }
    }

  @POST
  @Consumes(Array(MediaType.APPLICATION_XML))
  @Produces(Array(MediaType.APPLICATION_XML))
  @Path("/identitycard")
  @Transactional
  def returnIdentityCard(returnIdentityCardRequest: ReturnIdentityCardRequestDTO): AbstractResultDTO = returnIdentityCardRequest match {
    case ReturnIdentityCardRequestDTO(Some(identityCardBarcode), Some(envelopeBarcode)) => (ValidateBarcode(identityCardBarcode), ValidateBarcode(envelopeBarcode)) match {
      // both the identitycard barcode and the envelope barcode are valid
      case (ValidBarcode(identityCardBarcode), ValidBarcode(envelopeBarcode)) => (findIdentityCard(identityCardBarcode), findEnvelope(envelopeBarcode)) match {

        /**
          * 1. Both the identitycard and envelope are currently issued
          * 2. The identitycard is issued to the given envelope
          * 3. The identitycard has currently no borrowed games
          */
        case (Some(Left(lendIdentityCard@LendIdentityCard(_, _, _, _, _))), Some(Left(lendEnvelope))) if lendIdentityCard == lendEnvelope && lendIdentityCard.hasNoCurrentLendGames => {
          lendIdentityCardDao.returnIdentityCard(lendIdentityCard)
          ReturnIdentityCardSuccessDTO(lendIdentityCard.toIdentityCardDTO, lendIdentityCard.toEnvelopeDTO)
        }

        // the identitycard currently has borrowed games
        case (Some(Left(lendIdentityCard@LendIdentityCard(_, _, _, _, _))), Some(Left(lendEnvelope))) if lendIdentityCard == lendEnvelope && lendIdentityCard.hasCurrentLendGames =>
          IdentityCardHasIssuedGamesDTO(lendIdentityCard.toIdentityCardDTO, lendIdentityCard.toGameDTO)

        // the identity card is not issued to the given envelope
        case (Some(Left(lendIdentityCard)), Some(Left(otherLendIdentityCard))) if lendIdentityCard != otherLendIdentityCard =>
          IdentityCardEnvelopeNotBoundDTO(lendIdentityCard.toIdentityCardDTO, otherLendIdentityCard.toEnvelopeDTO, lendIdentityCard.toEnvelopeDTO)

        case (Some(Right(identityCard)), _) => LendingEntityInUseDTO(identityCard.toIdentityCardDTO, NotInUseDTO())
        case (_, Some(Right(envelope))) => LendingEntityInUseDTO(envelope.toEnvelopeDTO, NotInUseDTO())

        case (None, _) => LendingEntityNotExistsDTO(identityCardBarcode.toString)
        case (_, None) => LendingEntityNotExistsDTO(envelopeBarcode.toString)
      }

      case (InvalidBarcode(_), _) => IncorrectBarcodeDTO(identityCardBarcode)
      case (_, InvalidBarcode(_)) => IncorrectBarcodeDTO(envelopeBarcode)

      case _ => throw new BadRequestException("Invalid input request")
    }

    // wrong input
    case _ => throw new BadRequestException()
  }
}