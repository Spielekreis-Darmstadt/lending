package info.armado.ausleihe.database.access;

import info.armado.ausleihe.database.dataobjects.Barcode;
import info.armado.ausleihe.database.dataobjects.Envelope;
import info.armado.ausleihe.database.dataobjects.Game;
import info.armado.ausleihe.database.dataobjects.IdentityCard;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;

import lombok.extern.java.Log;

/**
 * This class contains all database access methods to work with identity cards 
 * 
 * @author Arndt
 *
 */
@Log
@RequestScoped
public class IdentityCardDAO extends EntityDao<IdentityCard, Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IdentityCardDAO() {
		super(IdentityCard.class);
	}

	/**
	 * This method receives a list of identity cards and returns the barcodes
	 * belonging to all all identity cards that are already persisted in the
	 * database.
	 * 
	 * @param identityCards
	 *            The identity cards
	 * @return A list of all barcodes belonging to all identity cards that are
	 *         already persisted in the given list
	 */
	@Transactional
	public Set<Barcode> selectAllIdentityCardsAlreadyPersisted(List<IdentityCard> identityCards) {
		List<Barcode> barcodes = identityCards.stream().map(game -> game.getBarcode()).collect(Collectors.toList());

		Set<Barcode> result = barcodes.stream().filter(this::exists).collect(Collectors.toSet());

		return result;
	}

	/**
	 * This method returns the identity card that is currently borrowing the
	 * given game. If the game is not borrowed at the moment, then null is
	 * returned.
	 * 
	 * @param game
	 *            The game for which the associated identity card is to be found
	 * @return The associated identity card to the given game, or null if the
	 *         game is currently not borrowed
	 */
	public IdentityCard selectFromGame(Game game) {
		IdentityCard result = null;

		String query = "select lg.lendIdentityCard.identityCard from LendGame lg where lg.game = :game and lg.returnTime is null";

		try {
			result = em.createQuery(query, IdentityCard.class).setParameter("game", game).getSingleResult();
		} catch (NoResultException nre) {
			log.log(Level.WARNING, String.format("Das Spiel \"%s\" mit der ID \"%d\" wurde nicht verliehen",
					game.getTitle(), game.getBarcode().toString()));
		} catch (NonUniqueResultException nure) {
			log.log(Level.SEVERE, String.format("Das Spiel \"%s\" mit der ID \"%d\" wurde mehrfach verliehen!",
					game.getTitle(), game.getBarcode().toString()));
		}

		return result;
	}

	/**
	 * This method returns the identity card currently belonging to the given
	 * envelope. If the envelope is currently not used (it is not bound to an
	 * identity card), than null is returned
	 * 
	 * @param envelope
	 *            The envelope for which the identity card should be found
	 * @return The currently associated identity card to the envelope or null is
	 *         the envelope is currently not used
	 */
	public IdentityCard selectFromEnvelope(Envelope envelope) {
		IdentityCard result = null;

		String query = "select lic.identityCard from LendIdentityCard lic where lic.envelope = :envelope and lic.returnTime is null";

		try {
			result = em.createQuery(query, IdentityCard.class).setParameter("envelope", envelope).getSingleResult();
		} catch (NoResultException nre) {
			log.log(Level.WARNING, String.format("Der Umschlag mit der ID \"%d\" wurde nicht ausgegeben",
					envelope.getBarcode().toString()));
		} catch (NonUniqueResultException nure) {
			log.log(Level.SEVERE, String.format("Der Umschlag mit der ID \"%d\" wurde mehrfach vergeben!",
					envelope.getBarcode().toString()));
		}

		return result;
	}

	/**
	 * This method checks if a given identity card is currently issued
	 * 
	 * @param idCard
	 *            The identity card that should be checked if it's currently
	 *            issued
	 * @return true if the identity card is currently issued, false otherwise
	 */
	public boolean isIdentityCardIssued(IdentityCard idCard) {
		String query = "select count(*) from LendIdentityCard lic where lic.identityCard = :idCard and lic.returnTime is null";

		long result = em.createQuery(query, Long.class).setParameter("idCard", idCard).getSingleResult();

		return result == 1;
	}

	/**
	 * This method returns all available identity cards in the database An
	 * identity card is available iff it is lendable
	 * 
	 * @return
	 */
	public List<IdentityCard> selectAllAvailable() {
		return em.createQuery("from IdentityCard idCard where idCard.available = true", IdentityCard.class)
				.getResultList();
	}

	/**
	 * This method returns all currently issued identity cards in the database
	 * 
	 * @return A list of all currently issued identity cards
	 */
	public List<IdentityCard> selectAllLend() {
		return em.createQuery("select lic.identityCard from LendIdentityCard lic where lic.returnTime is null",
				IdentityCard.class).getResultList();
	}

	/**
	 * This method returns the number of currently available identity cards. An
	 * identity card is available if its available property is set to true.
	 * 
	 * @return The number of all available identity cards
	 */
	public long selectNumberOfAvailableIdentityCards() {
		String query = "select count(*) from IdentityCard idCard where idCard.available = true";

		return em.createQuery(query, Long.class).getSingleResult();
	}
}
