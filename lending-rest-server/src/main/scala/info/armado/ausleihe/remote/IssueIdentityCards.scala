package info.armado.ausleihe.remote

import info.armado.ausleihe.database.dataobjects.ValidBarcode
import info.armado.ausleihe.remote.results.IncorrectBarcode
import javax.ws.rs.QueryParam
import info.armado.ausleihe.database.access.IdentityCardDAO
import info.armado.ausleihe.database.access.EnvelopeDAO
import info.armado.ausleihe.database.access.LendIdentityCardDAO
import info.armado.ausleihe.remote.results.LendingEntityNotExists
import info.armado.ausleihe.remote.results.IssueIdentityCardSuccess
import info.armado.ausleihe.util.DOExtensions._
import info.armado.ausleihe.remote.results.LendingEntityInUse
import info.armado.ausleihe.remote.dataobjects.inuse.IdentityCardInUse
import info.armado.ausleihe.remote.dataobjects.inuse.EnvelopeInUse
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import info.armado.ausleihe.database.dataobjects.ValidateBarcode
import info.armado.ausleihe.database.dataobjects.InvalidBarcode
import info.armado.ausleihe.database.dataobjects.InvalidBarcode
import info.armado.ausleihe.database.dataobjects.LendIdentityCard
import javax.ws.rs.POST
import javax.ws.rs.Consumes
import info.armado.ausleihe.remote.requests.IssueIdentityCardRequest
import info.armado.ausleihe.remote.results.AbstractResult
import javax.ws.rs.core.Response
import info.armado.ausleihe.database.dataobjects.IdentityCard
import info.armado.ausleihe.database.dataobjects.Envelope
import info.armado.ausleihe.database.dataobjects.Barcode
import javax.ws.rs.BadRequestException

@Path("/issue")
@RequestScoped
class IssueIdentityCards {
  @Inject var identityCardDao: IdentityCardDAO = _
  @Inject var envelopeDao: EnvelopeDAO = _
  @Inject var lendIdentityCardDao: LendIdentityCardDAO = _

  def findIdentityCard(identityCardBarcode: Barcode): Option[Either[LendIdentityCard, IdentityCard]] = Option(lendIdentityCardDao.selectCurrentByIdentityCardBarcode(identityCardBarcode)) match {
    case Some(lendIdentityCard) => Some(Left(lendIdentityCard))
    case None => Option(identityCardDao.selectActivatedByBarcode(identityCardBarcode)) match {
      case Some(identityCard) => Some(Right(identityCard))
      case None => None
    }
  }

  def findEnvelope(envelopeBarcode: Barcode): Option[Either[LendIdentityCard, Envelope]] = Option(lendIdentityCardDao.selectCurrentByEnvelopeBarcode(envelopeBarcode)) match {
    case Some(lendIdentityCard) => Some(Left(lendIdentityCard))
    case None => Option(envelopeDao.selectActivatedByBarcode(envelopeBarcode)) match {
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

        case (Some(Left(lic @ LendIdentityCard(_, _, _, _, _, _))), _) => LendingEntityInUse(lic.toIdentityCardData, IdentityCardInUse(lic.toEnvelopeData, lic.toGameData))
        case (_, Some(Left(lic @ LendIdentityCard(_, _, _, _, _, _)))) => LendingEntityInUse(lic.toEnvelopeData, EnvelopeInUse(lic.toIdentityCardData, lic.toGameData))

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