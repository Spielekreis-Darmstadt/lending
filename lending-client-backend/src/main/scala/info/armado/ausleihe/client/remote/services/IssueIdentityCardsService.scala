package info.armado.ausleihe.client.remote.services

import info.armado.ausleihe.client.transport.dataobjects.inuse.{EnvelopeInUseDTO, IdentityCardInUseDTO}
import info.armado.ausleihe.client.transport.requests.IssueIdentityCardRequestDTO
import info.armado.ausleihe.client.transport.results._
import info.armado.ausleihe.client.util.DTOExtensions._
import info.armado.ausleihe.database.access.{EnvelopeDao, IdentityCardDao, LendIdentityCardDao}
import info.armado.ausleihe.database.barcode._
import info.armado.ausleihe.database.entities.{Envelope, IdentityCard, LendIdentityCard}
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.MediaType

@Path("/issue")
@RequestScoped
class IssueIdentityCardsService {
  @Inject var identityCardDao: IdentityCardDao = _
  @Inject var envelopeDao: EnvelopeDao = _
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
        case Some(envelopeBarcode) => Some(Right(envelopeBarcode))
        case None => None
      }
    }

  @POST
  @Consumes(Array(MediaType.APPLICATION_XML))
  @Produces(Array(MediaType.APPLICATION_XML))
  @Path("/identitycard")
  @Transactional
  def issueIdentityCard(issueIdentityCardRequest: IssueIdentityCardRequestDTO): AbstractResultDTO = issueIdentityCardRequest match {
    case IssueIdentityCardRequestDTO(Some(identityCardBarcode), Some(envelopeBarcode)) => (ValidateBarcode(identityCardBarcode), ValidateBarcode(envelopeBarcode)) match {
      // both given barcodes (the identitycard barcode and the envelope barcode) are valid
      case (ValidBarcode(identityCardBarcode), ValidBarcode(envelopeBarcode)) => (findIdentityCard(identityCardBarcode), findEnvelope(envelopeBarcode)) match {
        // both the identitycard and the envelope are currently not issued
        case (Some(Right(identityCard)), Some(Right(envelope))) => {
          lendIdentityCardDao.issueIdentityCard(identityCard, envelope)
          IssueIdentityCardSuccessDTO(identityCard.toIdentityCardDTO, envelope.toEnvelopeDTO)
        }

        case (Some(Left(lic@LendIdentityCard(_, _, _, _, _))), _) => LendingEntityInUseDTO(lic.toIdentityCardDTO, IdentityCardInUseDTO(lic.toEnvelopeDTO, lic.toGameDTO))
        case (_, Some(Left(lic@LendIdentityCard(_, _, _, _, _)))) => LendingEntityInUseDTO(lic.toEnvelopeDTO, EnvelopeInUseDTO(lic.toIdentityCardDTO, lic.toGameDTO))

        case (None, _) => LendingEntityNotExistsDTO(identityCardBarcode.toString)
        case (_, None) => LendingEntityNotExistsDTO(envelopeBarcode.toString)
      }

      case (ValidBarcode(identityCardBarcode), InvalidBarcode(envelopeBarcode)) => IncorrectBarcodeDTO(envelopeBarcode)
      case (InvalidBarcode(identityCardBarcode), _) => IncorrectBarcodeDTO(identityCardBarcode)

      case _ => throw new BadRequestException("Invalid input request")
    }

    // wrong input
    case _ => throw new BadRequestException()
  }
}