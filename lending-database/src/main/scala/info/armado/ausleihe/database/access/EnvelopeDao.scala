package info.armado.ausleihe.database.access

import info.armado.ausleihe.database.entities.{Envelope, IdentityCard}

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.util.Try

/**
  * A data access object (DAO) to access [[Envelope]]s from a database
  *
  * @author Marc Arndt
  */
class EnvelopeDao extends EntityDao[Envelope, Integer](classOf[Envelope]) {
  /**
    * Queries the current envelope for a given identity card from the database
    *
    * @param identityCard The identity card whose envelope is queried
    * @return The found envelope or null, if the identity card is not issued
    */
  def selectFromIdentityCard(identityCard: IdentityCard): Option[Envelope] =
    Try(em.createQuery("select lic.envelope from LendIdentityCard lic where lic.identityCard = :identityCard and lic.returnTime is null",
      classOf[Envelope]).setParameter("identityCard", identityCard).getSingleResult
    ).toOption

  /**
    * Queries all currently issued envelopes from the database
    *
    * @return A list containing all currently issued envelopes
    */
  def selectAllLend(): List[Envelope] =
    em.createQuery("select lic.envelope from LendIdentityCard lic where lic.returnTime is null", classOf[Envelope])
      .getResultList.asScala.toList
}
