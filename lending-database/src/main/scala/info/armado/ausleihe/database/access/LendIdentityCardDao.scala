package info.armado.ausleihe.database.access

import java.lang.{Long => JLong}
import java.time.LocalDateTime
import javax.transaction.Transactional

import info.armado.ausleihe.database.barcode.Barcode
import info.armado.ausleihe.database.entities.{Envelope, IdentityCard, LendIdentityCard}

import scala.util.Try

/**
  * A data access object for accessing [[LendIdentityCard]] objects from a database
  *
  * @author Marc Arndt
  */
class LendIdentityCardDao extends LendEntityDao[LendIdentityCard, Integer](classOf[LendIdentityCard]) {
  /**
    * This method checks if the given identity card is currently issued.
    *
    * @param idCard
    * The identity card which should be checked if it's issued
    * @return True if the given identtiy card is currently issued
    */
  def isIdentityCardIssued(idCard: IdentityCard): Boolean = {
    val count = em.createQuery("select count(*) from LendIdentityCard lic where lic.identityCard = :idCard and lic.returnTime is null", classOf[JLong])
      .setParameter("idCard", idCard).getSingleResult

    count == 1
  }

  /**
    * This method checks if the given envelope is currently bound to an issued
    * identity card.
    *
    * @param envelope
    * The envelope to be checked
    * @return True if the given envelope is currently bound to an issued
    *         identity card
    */
  def isEnvelopeIssued(envelope: Envelope): Boolean = {
    val count = em.createQuery("select count(*) from LendIdentityCard lic where lic.envelope = :envelope and lic.returnTime is null", classOf[JLong])
      .setParameter("envelope", envelope).getSingleResult

    count == 1
  }

  /**
    * This method returns the current LendIdentityCard object belonging to both
    * the given identity card and the given envelope.
    *
    * @param idCard
    * The identity card, which belongs to the searched
    * LendIdentityCard object
    * @param envelope
    * The envelope, which belongs to the searched LendIdentityCard
    * object
    * @return The LendIdentityCard object currently belonging to both the given
    *         identity card and envelope, or null if no such LendIdentityCard
    *         exists
    */
  def selectCurrentByIdentityCardAndEnvelope(idCard: IdentityCard, envelope: Envelope): Option[LendIdentityCard] =
    Try(em.createQuery("from LendIdentityCard lic where lic.identityCard = :idCard and lic.envelope = :envelope and lic.returnTime is null", classOf[LendIdentityCard])
      .setParameter("idCard", idCard).setParameter("envelope", envelope).getSingleResult).toOption

  /**
    * This method returns the LendIdentityCard object currently belonging to
    * the given identity card.
    *
    * @param idCard
    * The identity card whose currently LendIdentityCard object is
    * searched
    * @return The current LendIdentityCard object belonging to the given
    *         identity card, or null if no such object exists
    */
  def selectCurrentByIdentityCard(idCard: IdentityCard): Option[LendIdentityCard] =
    Try(em.createQuery("from LendIdentityCard lic where lic.identityCard = :idCard and lic.returnTime is null", classOf[LendIdentityCard])
      .setParameter("idCard", idCard).getSingleResult).toOption

  /**
    * This method returns the LendIdentityCard currently belonging to an identity card with the given barcode.
    * If no such identity card exists or the identity card is currently not issued, null is returned
    *
    * @param barcode The barcode of an identity card
    * @return The current LendIdentityCard object belonging to the identity card barcode
    */
  def selectCurrentByIdentityCardBarcode(barcode: Barcode): Option[LendIdentityCard] =
    Try(em.createQuery("from LendIdentityCard lic where lic.identityCard.barcode = :barcode and lic.returnTime is null", classOf[LendIdentityCard])
      .setParameter("barcode", barcode).getSingleResult).toOption

  /**
    * This method returns the LendIdentityCard object currently belonging to
    * the given envelope-
    *
    * @param envelope
    * The envelope, whose current LendIdentityCard object is
    * requested
    * @return The current LendIdentityCard object belonging to the given
    *         envelope, or null if no such object exists
    */
  def selectCurrentByEnvelope(envelope: Envelope): Option[LendIdentityCard] =
    Try(em.createQuery("from LendIdentityCard lic where lic.envelope = :envelope and lic.returnTime is null", classOf[LendIdentityCard])
      .setParameter("envelope", envelope).getSingleResult).toOption

  /**
    * This method returns the LendIdentityCard currently belonging to an envelope with the given barcode.
    * If no such envelope exists or the envelope is currently not issued, null is returned
    *
    * @param barcode The barcode of an identity card
    * @return The current LendIdentityCard object belonging to the identity card barcode
    */
  def selectCurrentByEnvelopeBarcode(barcode: Barcode): Option[LendIdentityCard] =
    Try(em.createQuery("from LendIdentityCard lic where lic.envelope.barcode = :barcode and lic.returnTime is null", classOf[LendIdentityCard])
      .setParameter("barcode", barcode).getSingleResult).toOption

  /**
    * This method issued a given identity card to a given envelope.
    *
    * @param idCard
    * The identity card to be issued to the given envelope
    * @param envelope
    * The envelope to be bound to the given identity card
    */
  @Transactional
  def issueIdentityCard(idCard: IdentityCard, envelope: Envelope): Unit = insert(LendIdentityCard(idCard, envelope, LocalDateTime.now))

  /**
    * This method unbounds the given identity card and the given envelope.
    * Therefore this method must receive a pair of currently bound identity
    * card an envelope, otherwise it will throw an error.
    *
    * @param idCard
    * The to be unbounded identity card
    * @param envelope
    * The to be unbounded envelope
    */
  @Transactional
  def returnIdentityCard(idCard: IdentityCard, envelope: Envelope): Unit = selectCurrentByIdentityCardAndEnvelope(idCard, envelope).foreach(lic => returnIdentityCard(lic))
  
  /**
    * This method sets the given lend identity card as returned
    *
    * @param lendIdentityCard
    */
  @Transactional
  def returnIdentityCard(lendIdentityCard: LendIdentityCard): Unit = {
    lendIdentityCard.setReturnTime(LocalDateTime.now)

    update(lendIdentityCard)
  }

  @Transactional
  def updateOwner(lendIdentityCard: LendIdentityCard, owner: String): Unit = {
    lendIdentityCard.owner = owner

    update(lendIdentityCard)
  }
}
