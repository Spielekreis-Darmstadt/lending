/**
 * 
 */
package info.armado.ausleihe.database.access;

import info.armado.ausleihe.database.dataobjects.Barcode;
import info.armado.ausleihe.database.dataobjects.Envelope;
import info.armado.ausleihe.database.dataobjects.IdentityCard;
import info.armado.ausleihe.database.dataobjects.LendIdentityCard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;

import lombok.extern.java.Log;

/**
 * This class contains all database access methods to work with lend games
 * 
 * @author Arndt
 *
 */
@Log
@RequestScoped
public class LendIdentityCardDAO extends Dao<LendIdentityCard, Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LendIdentityCardDAO() {
		super(LendIdentityCard.class);
	}

	/**
	 * This method checks if the given identity card is currently issued.
	 * 
	 * @param idCard
	 *            The identity card which should be checked if it's issued
	 * @return True if the given identtiy card is currently issued
	 */
	public boolean isIdentityCardIssued(IdentityCard idCard) {
		String query = "select count(*) from LendIdentityCard lic where lic.identityCard = :idCard and lic.returnTime is null";

		long count = em.createQuery(query, Long.class).setParameter("idCard", idCard).getSingleResult();

		if (count > 1)
			log.log(Level.SEVERE, String.format("Identity card \"%s\" is issued multiple times concurrently",
					idCard.getBarcode().toString()));

		return count > 0;
	}

	/**
	 * This method checks if the given envelope is currently bound to an issued
	 * identity card.
	 * 
	 * @param envelope
	 *            The envelope to be checked
	 * @return True if the given envelope is currently bound to an issued
	 *         identity card
	 */
	public boolean isEnvelopeIssued(Envelope envelope) {
		String query = "select count(*) from LendIdentityCard lic where lic.envelope = :envelope and lic.returnTime is null";

		long count = em.createQuery(query, Long.class).setParameter("envelope", envelope).getSingleResult();

		if (count > 1)
			log.log(Level.SEVERE, String.format("Envelope \"%s\" is issued multiple times concurrently",
					envelope.getBarcode().toString()));

		return count > 0;
	}

	/**
	 * This method returns all currently issued identity cards An identity card
	 * is currently issued if its returnTime is null.
	 * 
	 * @return A list of all currently issued identity cards
	 */
	public List<LendIdentityCard> selectAllCurrentlyLend() {
		return em.createQuery("from LendIdentityCard lic where lic.returnTime is null", LendIdentityCard.class)
				.getResultList();
	}

	/**
	 * This method returns the current LendIdentityCard object belonging to both
	 * the given identity card and the given envelope.
	 * 
	 * @param idCard
	 *            The identity card, which belongs to the searched
	 *            LendIdentityCard object
	 * @param envelope
	 *            The envelope, which belongs to the searched LendIdentityCard
	 *            object
	 * @return The LendIdentityCard object currently belonging to both the given
	 *         identity card and envelope, or null if no such LendIdentityCard
	 *         exists
	 */
	public LendIdentityCard selectCurrentByIdentityCardAndEnvelope(IdentityCard idCard, Envelope envelope) {
		String query = "from LendIdentityCard lic where lic.identityCard = :idCard and lic.envelope = :envelope and lic.returnTime is null";

		LendIdentityCard lic = null;

		try {
			lic = em.createQuery(query, LendIdentityCard.class).setParameter("idCard", idCard)
					.setParameter("envelope", envelope).getSingleResult();
		} catch (NoResultException nre) {
			// do nothing
		} catch (NonUniqueResultException nure) {
			log.log(Level.SEVERE,
					String.format("Identity card \"%s\" and Envelope \"%s\" are issued to multiple times concurrently.",
							idCard.getBarcode().toString(), envelope.getBarcode().toString()),
					nure);
		}

		return lic;
	}

	/**
	 * This method returns the LendIdentityCard object currently belonging to
	 * the given identity card.
	 * 
	 * @param idCard
	 *            The identity card whose currently LendIdentityCard object is
	 *            searched
	 * @return The current LendIdentityCard object belonging to the given
	 *         identity card, or null if no such object exists
	 */
	public LendIdentityCard selectCurrentByIdentityCard(IdentityCard idCard) {
		String query = "from LendIdentityCard lic where lic.identityCard = :idCard and lic.returnTime is null";

		LendIdentityCard lic = null;

		try {
			lic = em.createQuery(query, LendIdentityCard.class).setParameter("idCard", idCard).getSingleResult();
		} catch (NoResultException nre) {
			// do nothing
		} catch (NonUniqueResultException nure) {
			log.log(Level.SEVERE, String.format("Identity card \"%s\" is issued to multiple envelopes concurrently.",
					idCard.getBarcode().toString()), nure);
		}

		return lic;
	}

	/**
	 * This method returns the LendIdentityCard currently belonging to an identity card with the given barcode.
	 * If no such identity card exists or the identity card is currently not issued, null is returned  
	 * @param barcode	The barcode of an identity card
	 * @return			The current LendIdentityCard object belonging to the identity card barcode
	 */
	public LendIdentityCard selectCurrentByIdentityCardBarcode(Barcode barcode) {
		String query = "from LendIdentityCard lic where lic.identityCard.barcode = :barcode and lic.returnTime is null";

		LendIdentityCard lic = null;

		try {
			lic = em.createQuery(query, LendIdentityCard.class).setParameter("barcode", barcode).getSingleResult();
		} catch (NoResultException nre) {
			// do nothing
		} catch (NonUniqueResultException nure) {
			log.log(Level.SEVERE, String.format("Identity card \"%s\" is issued to multiple envelopes concurrently.",
					barcode.toString()), nure);
		}

		return lic;
	}

	/**
	 * This method returns the LendIdentityCard object currently belonging to
	 * the given envelope-
	 * 
	 * @param envelope
	 *            The envelope, whose current LendIdentityCard object is
	 *            requested
	 * @return The current LendIdentityCard object belonging to the given
	 *         envelope, or null if no such object exists
	 */
	public LendIdentityCard selectCurrentByEnvelope(Envelope envelope) {
		String query = "from LendIdentityCard lic where lic.envelope = :envelope and lic.returnTime is null";

		LendIdentityCard lic = null;

		try {
			lic = em.createQuery(query, LendIdentityCard.class).setParameter("envelope", envelope).getSingleResult();
		} catch (NoResultException nre) {
			// do nothing
		} catch (NonUniqueResultException nure) {
			log.log(Level.SEVERE, String.format("Envelope \"%s\" is issued to multiple identity cards concurrently.",
					envelope.getBarcode().toString()), nure);
		}

		return lic;
	}
	
	/**
	 * This method returns the LendIdentityCard currently belonging to an envelope with the given barcode.
	 * If no such envelope exists or the envelope is currently not issued, null is returned  
	 * @param barcode	The barcode of an identity card
	 * @return			The current LendIdentityCard object belonging to the identity card barcode
	 */
	public LendIdentityCard selectCurrentByEnvelopeBarcode(Barcode barcode) {
		String query = "from LendIdentityCard lic where lic.envelope.barcode = :barcode and lic.returnTime is null";

		LendIdentityCard lic = null;

		try {
			lic = em.createQuery(query, LendIdentityCard.class).setParameter("barcode", barcode).getSingleResult();
		} catch (NoResultException nre) {
			// do nothing
		} catch (NonUniqueResultException nure) {
			log.log(Level.SEVERE, String.format("Envelope \"%s\" is issued to multiple identity cards concurrently.",
					barcode.toString()), nure);
		}

		return lic;
	}
	
	/**
	 * This method returns the number of currently issued identity cards. An
	 * identity card is currently issued if its returnTime is null.
	 * 
	 * @return The number of all currently issued identity cards
	 */
	public long selectNumberOfCurrentLendIdentityCards() {
		String query = "select count(*) from LendIdentityCard lic where lic.returnTime is null";

		return em.createQuery(query, Long.class).getSingleResult();
	}

	/**
	 * This method returns the total number of issued identity cards. The total
	 * number of issued identity cards contains all currently issued identity
	 * cards and all already returned identity cards.
	 * 
	 * @return The total number of issued identity cards
	 */
	public long selectNumberOfTotalLendIdentityCards() {
		String query = "select count(*) from LendIdentityCard lic";

		return em.createQuery(query, Long.class).getSingleResult();
	}

	/**
	 * This method issued a given identity card to a given envelope.
	 * 
	 * @param idCard
	 *            The identity card to be issued to the given envelope
	 * @param envelope
	 *            The envelope to be bound to the given identity card
	 */
	@Transactional
	public void issueIdentityCard(IdentityCard idCard, Envelope envelope) {
		LendIdentityCard lic = new LendIdentityCard();

		lic.setLendTime(LocalDateTime.now());

		lic.setIdentityCard(idCard);
		lic.setEnvelope(envelope);

		insert(lic);
	}

	/**
	 * This method unbounds the given identity card and the given envelope.
	 * Therefore this method must receive a pair of currently bound identity
	 * card an envelope, otherwise it will throw an error.
	 * 
	 * @param idCard
	 *            The to be unbounded identity card
	 * @param envelope
	 *            The to be unbounded envelope
	 */
	@Transactional
	public void returnIdentityCard(IdentityCard idCard, Envelope envelope) {
		LendIdentityCard lic = selectCurrentByIdentityCardAndEnvelope(idCard, envelope);

		lic.setReturnTime(LocalDateTime.now());

		update(lic);
	}
	
	/**
	 * This method sets the given lend identity card as returned
	 * @param lendIdentityCard
	 */
	@Transactional
	public void returnIdentityCard(LendIdentityCard lendIdentityCard) {
		lendIdentityCard.setReturnTime(LocalDateTime.now());

		update(lendIdentityCard);
	}
}
