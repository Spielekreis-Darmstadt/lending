package info.armado.ausleihe.database.access

import javax.persistence.{EntityManager, PersistenceContext}
import javax.transaction.Transactional

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.TraversableOnce
import scala.util.Try

/**
  * An abstract Data Access Object, which contains all basic functionality needed to work with an entity type [[Entity]].
  *
  * @param entityType The class of the entity [[Entity]]
  * @tparam Entity The type of entity
  * @tparam PK     The type of the primary key of the entity
  * @author Marc Arndt
  * @since 25.06.17
  */
abstract class Dao[Entity, PK](protected val entityType: Class[Entity]) extends Serializable {
  /**
    * The [[EntityManager]] for this [[Dao]].
    * This [[EntityManager]] will be injected by the application server
    */
  @PersistenceContext(unitName = "ausleihe")
  protected var em: EntityManager = _

  /**
    * Inserts a given [[Entity]] in the database
    *
    * @param entity The entity to be persisted/inserted
    */
  @Transactional
  def insert(entity: Entity): Unit = em.persist(entity)

  /**
    * Inserts a collection of entities in the database
    *
    * @param entities The collection of entities to be persisted/inserted
    */
  @Transactional
  def insert(entities: TraversableOnce[Entity]): Unit = entities.foreach(insert)

  /**
    * Deletes a given [[Entity]] from the database
    *
    * @param entity The entity to be removed/deleted
    */
  @Transactional
  def delete(entity: Entity): Unit = em.remove(entity)

  /**
    * Deletes a collection of entities from the database
    *
    * @param entities The collection of entities to be removed/deleted
    */
  @Transactional
  def delete(entities: TraversableOnce[Entity]): Unit = entities.foreach(delete)

  /**
    * Updates a given [[Entity]] in the database
    *
    * @param entity The to be updated entity with its new and updated values
    */
  @Transactional
  def update(entity: Entity): Unit = {
    em.merge(entity)
    /*
     * TODO: remove the flush() operation at a later time
     * The flush is a quick fix to have @Transactional operate correctly during the admin server tests
     */
    em.flush()
  }

  /**
    * Updates a collection of entities in the database
    *
    * @param entities The collection of entities to be updated
    */
  @Transactional
  def update(entities: TraversableOnce[Entity]): Unit = entities.foreach(update)

  /**
    * Refreshes a given [[Entity]] instance with the current state taken from the database
    *
    * @param entity The entity to be refreshed
    */
  def refresh(entity: Entity): Unit = em.refresh(entity)

  /**
    * Refreshes a collection of entities with the current state taken from the database
    *
    * @param entities The collection of entities to be refreshed
    */
  def refresh(entities: TraversableOnce[Entity]): Unit = entities.foreach(refresh)

  /**
    * Queries the database for an [[Entity]] with the given primary key
    *
    * @param pk The primary key of the queried entity
    * @return The found entity wrapped inside a [[Some]] instance or [[None]] if no such entity could be found
    */
  def select(pk: PK): Option[Entity] = Try(em.find(entityType, pk)).toOption

  /**
    * Queries a list of all entities of type `entityType` from the database
    *
    * @return A list containing all entities of type `entityType`
    */
  def selectAll(): List[Entity] =
    em.createQuery(s"from ${entityType.getSimpleName}", entityType).getResultList.asScala.toList

  /**
    * Clears the whole table containing the `entityType` inside the database
    */
  @Transactional
  def deleteAll(): Unit = em.createQuery(s"delete from ${entityType.getSimpleName}").executeUpdate
}
