/**
 * 
 */
package info.armado.ausleihe.database.access;

import info.armado.ausleihe.database.barcode.Barcode;
import info.armado.ausleihe.database.entities.Envelope;
import info.armado.ausleihe.database.entities.IdentityCard;

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
 * This class contains all database access methods to work with envelopes 
 * 
 * @author Arndt
 *
 */
@Log
@RequestScoped
public class EnvelopeDAO extends EntityDao<Envelope, Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EnvelopeDAO() {
		super(Envelope.class);
	}

	/**
	 * This method receives a list of envelopes and returns all barcodes from
	 * all contained envelopes that are already persisted in the database.
	 * 
	 * @param envelopes
	 *            A list of envelopes, in which the persisted envelopes are
	 *            searched
	 * @return A list of barcodes whose envelopes are already persisted.
	 */
	@Transactional
	public Set<Barcode> selectAllEnvelopesAlreadyPersisted(List<Envelope> envelopes) {
		List<Barcode> barcodes = envelopes.stream().map(game -> game.getBarcode()).collect(Collectors.toList());

		Set<Barcode> result = barcodes.stream().filter(this::exists).collect(Collectors.toSet());

		return result;
	}

	/**
	 * This method returns the currently to a given idCard issued envelope
	 * 
	 * @param identityCard
	 *            The identity card to the searched envelope
	 * @return The envelope belonging to the given id card or null if the id
	 *         card is currently not issued
	 */
	public Envelope selectFromIdentityCard(IdentityCard identityCard) {
		Envelope result = null;

		try {
			result = em.createQuery(
					"select lic.envelope from LendIdentityCard lic where lic.identityCard = :identityCard and lic.returnTime is null",
					Envelope.class).setParameter("identityCard", identityCard).getSingleResult();
		} catch (NoResultException nre) {
			log.log(Level.WARNING, String.format("Der Ausweis mit der ID \"%d\" wurde nicht ausgegeben",
					identityCard.getBarcode().toString()));
		} catch (NonUniqueResultException nure) {
			log.log(Level.SEVERE, String.format("Der Ausweis mit der ID \"%d\" wurde mehrfach vergeben!",
					identityCard.getBarcode().toString()));
		}

		return result;
	}

	/**
	 * This method returns a list of all available envelopes in the database. An
	 * envelope is available if it's currently lendable.
	 * 
	 * @return A list of all currently available envelopes
	 */
	public List<Envelope> selectAllAvailable() {
		return em.createQuery("from Envelope e where e.available = true", Envelope.class).getResultList();
	}

	/**
	 * This method returns all currently lend/used/issued envelopes.
	 * 
	 * @return A list of all currently lend envelopes
	 */
	public List<Envelope> selectAllLend() {
		return em.createQuery("select lic.envelope from LendIdentityCard lic where lic.returnTime is null",
				Envelope.class).getResultList();
	}
}
