package info.armado.ausleihe.remote

import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.MediaType

import info.armado.ausleihe.database.access.{EnvelopeDAO, IdentityCardDAO, LendGameDAO, LendIdentityCardDAO}
import info.armado.ausleihe.database.barcode._
import info.armado.ausleihe.database.entities.{Envelope, IdentityCard, LendIdentityCard}
import info.armado.ausleihe.remote.dataobjects.inuse.NotInUse
import info.armado.ausleihe.remote.requests.ReturnIdentityCardRequest
import info.armado.ausleihe.remote.results._
import info.armado.ausleihe.util.DOExtensions._

@Path("/return")
@RequestScoped
class ReturnIdentityCards {
  @Inject var identityCardDao: IdentityCardDAO = _
  @Inject var envelopeDao: EnvelopeDAO = _
  @Inject var lendGameDao: LendGameDAO = _
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
  def returnIdentityCard(returnIdentityCardRequest: ReturnIdentityCardRequest): AbstractResult = returnIdentityCardRequest match {
    case ReturnIdentityCardRequest(Some(identityCardBarcode), Some(envelopeBarcode)) => (ValidateBarcode(identityCardBarcode), ValidateBarcode(envelopeBarcode)) match {
      // both the identitycard barcode and the envelope barcode are valid
      case (ValidBarcode(identityCardBarcode), ValidBarcode(envelopeBarcode)) => (findIdentityCard(identityCardBarcode), findEnvelope(envelopeBarcode)) match {

        /**
         * 1. Both the identitycard and envelope are currently issued
         * 2. The identitycard is issued to the given envelope
         * 3. The identitycard has currently no borrowed games
         */
        case (Some(Left(lendIdentityCard @ LendIdentityCard(_, _, _, _, _, Nil))), Some(Left(lendEnvelope))) if lendIdentityCard == lendEnvelope => {
          lendIdentityCardDao.returnIdentityCard(lendIdentityCard)
          ReturnIdentityCardSuccess(lendIdentityCard.toIdentityCardData, lendIdentityCard.toEnvelopeData)
        }

        // the identitycard currently has borrowed games
        case (Some(Left(lendIdentityCard @ LendIdentityCard(_, _, _, _, _, List(_, _*)))), Some(Left(lendEnvelope))) if lendIdentityCard == lendEnvelope =>
          IdentityCardHasIssuedGames(lendIdentityCard.toIdentityCardData, lendIdentityCard.toGameData)

        // the identity card is not issued to the given envelope
        case (Some(Left(lendIdentityCard)), Some(Left(otherLendIdentityCard))) if lendIdentityCard != otherLendIdentityCard =>
          IdentityCardEnvelopeNotBound(lendIdentityCard.toIdentityCardData, otherLendIdentityCard.toEnvelopeData, lendIdentityCard.toEnvelopeData)

        case (Some(Right(identityCard)), _) => LendingEntityInUse(identityCard.toIdentityCardData, NotInUse())
        case (_, Some(Right(envelope))) => LendingEntityInUse(envelope.toEnvelopeData, NotInUse())

        case (None, _) => LendingEntityNotExists(identityCardBarcode)
        case (_, None) => LendingEntityNotExists(envelopeBarcode)
      }

      case (InvalidBarcode(identityCardBarcode), _) => IncorrectBarcode(identityCardBarcode)
      case (_, InvalidBarcode(envelopeBarcode)) => IncorrectBarcode(envelopeBarcode)
    }

    // wrong input
    case _ => throw new BadRequestException()
  }
}