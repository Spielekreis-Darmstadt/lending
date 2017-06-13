package info.armado.ausleihe.database.access;

import java.util.logging.Level;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import info.armado.ausleihe.database.dataobjects.Barcode;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

@Log
@NoArgsConstructor
public abstract class EntityDao<Entity, PK> extends Dao<Entity, PK> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected EntityDao(Class<Entity> type) {
		super(type);
	}
	
	/**
	 * This method returns an object of type Entity belonging to a given barcode.
	 * 
	 * @param barcode
	 *            The barcode, whose game object should be returned
	 * @return The object belonging to the given barcode or null if no such
	 *         object exists in the database
	 */
	public Entity selectByBarcode(Barcode barcode) {
		Entity result = null;

		String query = String.format("from %s entity where entity.barcode = :barcode", type.getSimpleName());

		try {
			result = em.createQuery(query, type).setParameter("barcode", barcode).getSingleResult();
		} catch (NoResultException nre) {
			// do nothing
		} catch (NonUniqueResultException nure) {
			log.log(Level.SEVERE, String.format("Eine Entity vom Typ %s mit der ID \"%d\" existiert mehrfach!", type.getSimpleName(), barcode.toString()));
		}

		return result;
	}
	
	/**
	 * This method returns an object of type Entity belonging to a given barcode which is currently activated.
	 * 
	 * @param barcode
	 *            The barcode, whose game object should be returned
	 * @return The object belonging to the given barcode which is currently activated, or null if no such
	 *         object exists in the database
	 */
	public Entity selectActivatedByBarcode(Barcode barcode) {
		Entity result = null;

		String query = String.format("from %s entity where entity.barcode = :barcode and entity.available = true", type.getSimpleName());

		try {
			result = em.createQuery(query, type).setParameter("barcode", barcode).getSingleResult();
		} catch (NoResultException nre) {
			// do nothing
		} catch (NonUniqueResultException nure) {
			log.log(Level.SEVERE, String.format("Eine Entity vom Typ %s mit der ID \"%d\" existiert mehrfach!", type.getSimpleName(), barcode.toString()));
		}

		return result;
	}
	
	/**
	 * This method checks if an object of type Entity with the given barcode exists in the
	 * database.
	 * 
	 * @param barcode
	 *            The barcode, to which a game should exist
	 * @return True, if an object with the given barcode exists in the database,
	 *         false otherwise
	 */
	public boolean exists(Barcode barcode) {
		String query = String.format("select count(*) from %s entity where entity.barcode = :barcode", type.getSimpleName());

		long count = em.createQuery(query, Long.class).setParameter("barcode", barcode).getSingleResult();

		return count > 0;
	}
}
