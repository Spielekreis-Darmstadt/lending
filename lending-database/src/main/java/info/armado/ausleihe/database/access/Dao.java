/**
 * 
 */
package info.armado.ausleihe.database.access;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import lombok.NoArgsConstructor;

/**
 * @author Arndt
 *
 */
@NoArgsConstructor
public abstract class Dao<Entity, PK> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Class<Entity> type;

	@PersistenceContext(unitName = "ausleihe")
	protected EntityManager em;

	protected Dao(Class<Entity> type) {
		this.type = type;
	}

	/**
	 * This method persists a list of entities in a single transaction. This
	 * means that if one of the given entities can not be persisted the call
	 * fails and all already persisted entities from this method calls get
	 * rerolled
	 * 
	 * @param games
	 *            The entities to be persisted in the database
	 */
	@Transactional
	public void insert(List<Entity> entities) {
		for (Entity entity : entities) {
			this.insert(entity);
		}
	}

	/**
	 * This method persists a given object of type Entity.
	 * 
	 * @param entity
	 *            The entity to be persisted
	 */
	public void insert(Entity entity) {
		em.persist(entity);
	}

	/**
	 * This method removes/deletes a given object of type Entity.
	 * 
	 * @param entity
	 *            The entity to be removed
	 */
	public void delete(Entity entity) {
		em.remove(entity);
	}

	/**
	 * This method updates a given object of type Entity.
	 * 
	 * @param entity
	 *            The entity to be updated
	 */
	@Transactional
	public void update(Entity entity) {
		em.merge(entity);
	}

	/**
	 * This method refreshes the given object of type Entity. In this process
	 * its state may be overridden.
	 * 
	 * @param entity
	 *            The entity to be refreshed
	 */
	public void refresh(Entity entity) {
		em.refresh(entity);
	}

	/**
	 * This method returns the entity corresponding to the given primary key.
	 * 
	 * @param pk
	 *            The primary key from the searched object of type Entity
	 * @return The searched object with the given primary key or null if no such
	 *         object exists
	 */
	public Entity select(PK pk) {
		return em.find(type, pk);
	}

	/**
	 * This method returns all persisted objects of type Entity.
	 * 
	 * @return A list of all persisted objects of type Entity
	 */
	public List<Entity> selectAll() {
		return em.createQuery("from " + type.getSimpleName(), type).getResultList();
	}
	
	/**
	 * This method deletes all persisted objects of type Entity.
	 */
	@Transactional
	public void deleteAll() {
		em.createQuery(String.format("delete from %s", type.getSimpleName())).executeUpdate();
	}
}
