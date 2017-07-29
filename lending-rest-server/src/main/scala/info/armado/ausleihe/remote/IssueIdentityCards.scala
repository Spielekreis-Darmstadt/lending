package info.armado.ausleihe.remote

import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.MediaType

import info.armado.ausleihe.database.access.{EnvelopeDao, IdentityCardDao, LendIdentityCardDao}
import info.armado.ausleihe.database.barcode._
import info.armado.ausleihe.database.entities.{Envelope, IdentityCard, LendIdentityCard}
import info.armado.ausleihe.remote.dataobjects.inuse.{EnvelopeInUse, IdentityCardInUse}
import info.armado.ausleihe.remote.requests.IssueIdentityCardRequest
import info.armado.ausleihe.remote.results._
import info.armado.ausleihe.util.DOExtensions._

@Path("/issue")
@RequestScoped
class IssueIdentityCards {
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
  def issueIdentityCard(issueIdentityCardRequest: IssueIdentityCardRequest): AbstractResult = issueIdentityCardRequest match {
    case IssueIdentityCardRequest(Some(identityCardBarcode), Some(envelopeBarcode)) => (ValidateBarcode(identityCardBarcode), ValidateBarcode(envelopeBarcode)) match {
      // both given barcodes (the identitycard barcode and the envelope barcode) are valid
      case (ValidBarcode(identityCardBarcode), ValidBarcode(envelopeBarcode)) => (findIdentityCard(identityCardBarcode), findEnvelope(envelopeBarcode)) match {
        // both the identitycard and the envelope are currently not issued
        case (Some(Right(identityCard)), Some(Right(envelope))) => {
          lendIdentityCardDao.issueIdentityCard(identityCard, envelope)
          IssueIdentityCardSuccess(identityCard.toIdentityCardData, envelope.toEnvelopeData)
        }

        case (Some(Left(lic@LendIdentityCard(_, _, _, _, _))), _) => LendingEntityInUse(lic.toIdentityCardData, IdentityCardInUse(lic.toEnvelopeData, lic.toGameData))
        case (_, Some(Left(lic@LendIdentityCard(_, _, _, _, _)))) => LendingEntityInUse(lic.toEnvelopeData, EnvelopeInUse(lic.toIdentityCardData, lic.toGameData))

        case (None, _) => LendingEntityNotExists(identityCardBarcode)
        case (_, None) => LendingEntityNotExists(envelopeBarcode)
      }

      case (ValidBarcode(identityCardBarcode), InvalidBarcode(envelopeBarcode)) => IncorrectBarcode(envelopeBarcode)
      case (InvalidBarcode(identityCardBarcode), _) => IncorrectBarcode(identityCardBarcode)
    }

    // wrong input
    case _ => throw new BadRequestException()
  }
}