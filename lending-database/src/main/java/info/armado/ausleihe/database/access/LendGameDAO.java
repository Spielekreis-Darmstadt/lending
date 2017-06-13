package info.armado.ausleihe.database.access;

import info.armado.ausleihe.database.dataobjects.Barcode;
import info.armado.ausleihe.database.dataobjects.Game;
import info.armado.ausleihe.database.dataobjects.LendGame;
import info.armado.ausleihe.database.dataobjects.LendIdentityCard;
import info.armado.ausleihe.database.enums.Prefix;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;

import lombok.extern.java.Log;

/**
 * This class contains all database access methods to work with issued/lend identity cards and envelopes
 * 
 * @author Arndt
 *
 */
@Log
@RequestScoped
public class LendGameDAO extends Dao<LendGame, Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LendGameDAO() {
		super(LendGame.class);
	}

	/**
	 * This method returns a list of LendGame objects corresponding to all
	 * games, that are currently borrowed.
	 * 
	 * @return A list of all currently lent games
	 */
	public List<LendGame> selectAllCurrentLendGames() {
		String query = "from LendGame lg where lg.returnTime is null";

		return em.createQuery(query, LendGame.class).getResultList();
	}

	/**
	 * This method checks if a given game is currently borrowed.
	 * 
	 * @param game
	 *            The game to be checked if it's currently borrowed
	 * @return True if the given game is currently borrowed, false otherwise
	 */
	public boolean isGameLend(Game game) {
		String query = "select count(*) from LendGame lg where lg.game = :game and lg.returnTime is null";

		long count = em.createQuery(query, Long.class).setParameter("game", game).getSingleResult();

		if (count > 1)
			log.log(Level.SEVERE,
					String.format("Game \"%s\" is issued multiple times concurrently", game.getBarcode().toString()));

		return count > 0;
	}

	/**
	 * This method returns the current LendGame object corresponding to the
	 * given Game. The current LendGame object has a returnTime of null. If no
	 * such object exists it means, that the given Game is currently not lent.
	 * 
	 * @param game
	 *            The game whose LendGame object is wanted
	 * @return The current LendGame object belonging to the given Game object,
	 *         or null if no such LendGame object exists
	 */
	public LendGame selectLendGameByGame(Game game) {
		String query = "from LendGame lg where lg.game = :game and lg.returnTime is null";

		LendGame lendGame = null;

		try {
			lendGame = em.createQuery(query, LendGame.class).setParameter("game", game).getSingleResult();
		} catch (NoResultException nre) {
			// do nothing
		} catch (NonUniqueResultException nure) {
			log.log(Level.SEVERE, String.format("Game \"%s\" is issued to multiple identityCards concurrently.",
					game.getBarcode().toString()), nure);
		}

		return lendGame;
	}
	
	/**
	 * This method returns the current LendGame object corresponding to a game with the given barcode with a returnTime of null.
	 * If no
	 * such object exists it means, that the given Game is either currently not lent or doesn't exist.
	 * 
	 * @param barcode The barcode of a game whose current LendGame object is searched
	 * @return
	 */
	public LendGame selectLendGameByGameBarcode(Barcode barcode) {
		String query = "from LendGame lg where lg.game.barcode = :barcode and lg.returnTime is null";

		LendGame lendGame = null;

		try {
			lendGame = em.createQuery(query, LendGame.class).setParameter("barcode", barcode).getSingleResult();
		} catch (NoResultException nre) {
			// do nothing
		} catch (NonUniqueResultException nure) {
			log.log(Level.SEVERE, String.format("Game \"%s\" is issued to multiple identityCards concurrently.",
					barcode.toString()), nure);
		}

		return lendGame;
	}
	
	/**
	 * This method returns the current LendIdentityCard object for a given game.
	 * The current LendIdentityCard belongs to a LendGame object with a
	 * returnTime of null. If no such object exists it means, that the given
	 * game is currently not lent.
	 * 
	 * @param game
	 *            The game whose current LendIdentityCard is wanted
	 * @return The current LendIdentityCard belonging to the given Game, or null
	 *         if no such LendIdentityCard exists
	 */
	public LendIdentityCard selectCurrentLendIdentityCardByGame(Game game) {
		String query = "select lg.lendIdentityCard from LendGame lg where lg.game = :game and lg.returnTime is null";

		LendIdentityCard lic = null;

		try {
			lic = em.createQuery(query, LendIdentityCard.class).setParameter("game", game).getSingleResult();
		} catch (NoResultException nre) {
			// do nothing
		} catch (NonUniqueResultException nure) {
			log.log(Level.SEVERE, String.format("Game \"%s\" is issued to multiple identityCards concurrently.",
					game.getBarcode().toString()), nure);
		}

		return lic;
	}

	/**
	 * This method checks, if the given LendIdentityCard has currently borrowed
	 * one or more games.
	 * 
	 * @param lic
	 *            The LendIdentityCard which should be checked for having
	 *            borrowed Games
	 * @return True, if the given LendIdentityCard has currently borrowed one or
	 *         more games
	 */
	public boolean hasLendIdentityCardCurrentlyLendGames(LendIdentityCard lic) {
		String query = "select count(*) from LendGame lg where lg.lendIdentityCard = :lic and lg.returnTime is null";

		long count = em.createQuery(query, Long.class).setParameter("lic", lic).getSingleResult();

		return count > 0;
	}

	/**
	 * This method returns all currently borrowed games by the given
	 * LendIdentityCard. If the given LendIdentityCard has currently borrowed
	 * zero games an empty list is returned
	 * 
	 * @param lic
	 *            The LendIdentityCard for which the currently borrowed games
	 *            are requested
	 * @return A list of currently borrowed games by the given LendIdentityCard
	 */
	public List<LendGame> selectCurrentLendGamesByLendIdentityCard(LendIdentityCard lic) {
		String query = "from LendGame lg where lg.lendIdentityCard = :lic and lg.returnTime is null";

		List<LendGame> games = em.createQuery(query, LendGame.class).setParameter("lic", lic).getResultList();

		return games;
	}

	/**
	 * This method returns the number of currently lend games.
	 * 
	 * @return The number of currently lend games
	 */
	public long selectNumberOfCurrentLendGames() {
		String query = "select count(*) from LendGame lg where lg.returnTime is null";

		return em.createQuery(query, Long.class).getSingleResult();
	}

	/**
	 * This method returns the number of currently lend games provided by the
	 * BDKJ. The games from BDKJ all start with the prefix 22.
	 * 
	 * @return The number of currently lend games from BDKJ
	 */
	public long selectNumberOfCurrentLendBDKJGames() {
		String query = "select count(*) from LendGame lg where lg.returnTime is null and lg.game.barcode like :barcode";

		return em.createQuery(query, Long.class).setParameter("barcode", Barcode.createWildcard(Prefix.BDKJ))
				.getSingleResult();
	}

	/**
	 * This method returns the number of currently lend games from the
	 * Spielekreis. The games from Spielekreis all start with the prefix 11.
	 * 
	 * @return The number of currently lend games from Spielekreis
	 */
	public long selectNumberOfCurrentLendSpielekreisGames() {
		String query = "select count(*) from LendGame lg where lg.returnTime is null and lg.game.barcode like :barcode";

		return em.createQuery(query, Long.class).setParameter("barcode", Barcode.createWildcard(Prefix.Spielekreis))
				.getSingleResult();
	}

	/**
	 * This method returns the total number of lend games. To the total number
	 * of lend games belong the current lend games and the already returned
	 * games.
	 * 
	 * @return The total number of lend games
	 */
	public long selectNumberOfTotalLendGames() {
		String query = "select count(*) from LendGame lg";

		return em.createQuery(query, Long.class).getSingleResult();
	}

	/**
	 * This method issues all games in the given games-list to the given
	 * LendIdentityCard.
	 * 
	 * @param games
	 *            The games to be lent
	 * @param lic
	 *            The identity card that should be assigned to the given games
	 */
	@Transactional
	public void issueGames(List<Game> games, LendIdentityCard lic) {
		for (Game game : games) {
			LendGame lendGame = new LendGame();

			lendGame.setLendTime(LocalDateTime.now());

			lendGame.setGame(game);
			lendGame.setLendIdentityCard(lic);

			insert(lendGame);
		}
	}

	/**
	 * This method sets the given game as returned Therefore this method must
	 * receive a currently borrowed game, otherwise it will throw an error.
	 * 
	 * @param game
	 *            The game to be returned
	 */
	@Transactional
	public void returnGame(Game game) {
		LendGame lendGame = selectLendGameByGame(game);

		lendGame.setReturnTime(LocalDateTime.now());

		update(lendGame);
	}
	
	/**
	 * This method sets the given lend game as returned
	 * @param lendGame
	 */
	@Transactional
	public void returnGame(LendGame lendGame) {
		lendGame.setReturnTime(LocalDateTime.now());

		update(lendGame);
	}
}
