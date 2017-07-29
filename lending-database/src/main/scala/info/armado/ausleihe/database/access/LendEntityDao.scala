package info.armado.ausleihe.database.access

import java.lang.{Long => JLong}

import scala.collection.JavaConverters.asScalaBufferConverter

abstract class LendEntityDao[Entity, PK](entityType: Class[Entity]) extends Dao[Entity, PK](entityType) {
  /**
    * This method returns a list of [[Entity]] objects, that are currently borrowed.
    *
    * @return A list of all currently lend entities
    */
  def selectAllCurrentLend: List[Entity] =
    em.createQuery(s"from ${entityType.getSimpleName} lendEntity where lendEntity.returnTime is null", entityType)
      .getResultList.asScala.toList

  /**
    * This method returns the number of currently lend entities.
    *
    * @return The number of currently lend entities
    */
  def selectNumberOfCurrentLendEntities: Long =
    em.createQuery(s"select count(*) from ${entityType.getSimpleName} lendEntity where lendEntity.returnTime is null", classOf[JLong])
      .getSingleResult

  /**
    * This method returns the total number of lend entities. To the total number
    * of lend entities belong the current lend entities and the already returned
    * entities.
    *
    * @return The total number of lend entities
    */
  def selectNumberOfTotalLendEntities: Long =
    em.createQuery(s"select count(*) from ${entityType.getSimpleName} lendEntity", classOf[JLong]).getSingleResult
}
