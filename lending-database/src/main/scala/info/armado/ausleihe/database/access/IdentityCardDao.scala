package info.armado.ausleihe.database.access

import java.lang.{Long => JLong}

import com.typesafe.scalalogging.Logger
import info.armado.ausleihe.database.entities.{Envelope, Game, IdentityCard}

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.util.Try

/**
  * A data access object to access [[IdentityCard]] objects from the database
  */
class IdentityCardDao extends EntityDao[IdentityCard, Integer](classOf[IdentityCard]) {

  /**
    * This method returns the identity card that is currently borrowing the
    * given game. If the game is not borrowed at the moment, then null is
    * returned.
    *
    * @param game The game for which the associated identity card is to be found
    * @return The associated identity card to the given game, or null if the
    *         game is currently not borrowed
    */
  def selectFromGame(game: Game): Option[IdentityCard] =
    Try(em.createQuery("select lg.lendIdentityCard.identityCard from LendGame lg where lg.game = :game and lg.returnTime is null", classOf[IdentityCard])
      .setParameter("game", game).getSingleResult).toOption

  /**
    * This method returns the identity card currently belonging to the given
    * envelope. If the envelope is currently not used (it is not bound to an
    * identity card), than null is returned
    *
    * @param envelope The envelope for which the identity card should be found
    * @return The currently associated identity card to the envelope or null is
    *         the envelope is currently not used
    */
  def selectFromEnvelope(envelope: Envelope): Option[IdentityCard] =
    Try(em.createQuery("select lic.identityCard from LendIdentityCard lic where lic.envelope = :envelope and lic.returnTime is null", classOf[IdentityCard])
      .setParameter("envelope", envelope).getSingleResult).toOption

  /**
    * This method checks if a given identity card is currently issued
    *
    * @param idCard The identity card that should be checked if it's currently
    *               issued
    * @return true if the identity card is currently issued, false otherwise
    */
  def isIdentityCardIssued(idCard: IdentityCard): Boolean = {
    val result = em.createQuery("select count(*) from LendIdentityCard lic where lic.identityCard = :idCard and lic.returnTime is null", classOf[JLong])
      .setParameter("idCard", idCard).getSingleResult

    result == 1
  }

  /**
    * This method returns all currently issued identity cards in the database
    *
    * @return A list of all currently issued identity cards
    */
  def selectAllLend: List[IdentityCard] =
    em.createQuery("select lic.identityCard from LendIdentityCard lic where lic.returnTime is null", classOf[IdentityCard])
      .getResultList.asScala.toList
}
