package info.armado.ausleihe.database.access

import java.lang.{Long => JLong}
import javax.transaction.Transactional

import info.armado.ausleihe.database.barcode.Barcode
import info.armado.ausleihe.database.entities.HasBarcode

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.util.Try

/**
  * An abstract Data Access Object for entities, like games, identity cards and envelopes which have a barcode.
  *
  * @param entityType The class of the entity [[Entity]], this class needs to have a barcode
  * @tparam Entity The type of entity
  * @tparam PK     The type of the primary key of the entity
  * @author Marc Arndt
  * @since 25.06.17
  */
abstract class EntityDao[Entity <: HasBarcode, PK](entityType: Class[Entity]) extends Dao[Entity, PK](entityType) {
  /**
    * Queries the database for an [[Entity]], which belongs to the given `barcode`
    *
    * @param barcode The barcode of the queried entity
    * @return The object belonging to the given barcode wrapped inside a [[Some]] instance or [[None]], if no such
    *         object exists in the database
    */
  def selectByBarcode(barcode: Barcode): Option[Entity] =
    Try(
      em.createQuery(s"from ${entityType.getSimpleName} entity where entity.barcode = :barcode", entityType)
        .setParameter("barcode", barcode).getSingleResult
    ).toOption

  /**
    * Queries the database for an activated [[Entity]], which belongs to the given `barcode`
    *
    * @param barcode The barcode of the queried entity
    * @return The object belonging to the given barcode wrapped inside a [[Some]] instance or [[None]], if no such
    *         object exists in the database
    */
  def selectActivatedByBarcode(barcode: Barcode): Option[Entity] =
    Try(
      em.createQuery(s"from ${entityType.getSimpleName} entity where entity.barcode = :barcode and entity.available = true", entityType)
        .setParameter("barcode", barcode).getSingleResult
    ).toOption

  /**
    * Queries the database for a list of all available entities
    *
    * @return A list of all available entities
    */
  def selectAllAvailable(): List[Entity] =
    em.createQuery(s"from ${entityType.getSimpleName} entity where entity.available = true", entityType)
      .getResultList.asScala.toList

  /**
    * Filters the already persisted barcodes from a given list of entities with barcodes and returns them
    *
    * @param envelopes A list of entities with a barcode, containing already persisted and not not persisted entities
    * @return A list of already persisted barcodes from the barcodes of the given entities
    */
  @Transactional
  def selectAllAlreadyPersisted(envelopes: List[Entity]): Set[Barcode] = envelopes.map(_.barcode).filter(barcode => exists(barcode)).toSet

  /**
    * This method returns the number of currently available entites. An
    * entity is available, if its available property is set to true.
    *
    * @return The number of all available entities
    */
  def selectAvailableNumber: Long =
    em.createQuery(s"select count(*) from ${entityType.getSimpleName} entity where entity.available = true", classOf[JLong]).getSingleResult

  /**
    * Queries the database if an [[Entity]] for the given `barcode` exists
    *
    * @param barcode The barcode of the checked entity
    * @return True if an entity with the given `barcode` exists, false otherwise
    */
  def exists(barcode: Barcode): Boolean = {
    val count = em
      .createQuery(s"select count(*) from ${entityType.getSimpleName} entity where entity.barcode = :barcode", classOf[JLong])
      .setParameter("barcode", barcode).getSingleResult

    count > 0
  }

  /**
    * Tries to activate the [[Entity]] with the given `barcode`.
    * This will fail if no [[Entity]] with the given `barcode` exists
    *
    * @param barcode The barcode of the entity to activate
    * @return True if the the entity could be activated, false otherwise
    */
  @Transactional
  def activate(barcode: Barcode): Boolean = {
    val updateCount = em
      .createQuery(s"update ${entityType.getSimpleName} entity set entity.available = true where entity.barcode = :barcode")
      .setParameter("barcode", barcode).executeUpdate()

    updateCount == 1
  }

  /**
    * Tries to deactivate the [[Entity]] with the given `barcode`.
    * This will fail if no [[Entity]] with the given `barcode` exists
    *
    * @param barcode The barcode of the entity to deactivate
    * @return True if the the entity could be deactivated, false otherwise
    */
  @Transactional
  def deactivate(barcode: Barcode): Boolean = {
    val updateCount = em
      .createQuery(s"update ${entityType.getSimpleName} entity set entity.available = false where entity.barcode = :barcode")
      .setParameter("barcode", barcode).executeUpdate()

    updateCount == 1
  }
}
