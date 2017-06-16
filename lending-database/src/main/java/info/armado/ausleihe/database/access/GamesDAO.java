/**
 * 
 */
package info.armado.ausleihe.database.access;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import info.armado.ausleihe.database.barcode.Barcode;
import info.armado.ausleihe.database.entities.Game;
import info.armado.ausleihe.database.enums.Prefix;
import info.armado.ausleihe.database.objects.GameInfo;
import info.armado.ausleihe.database.util.AndCondition;
import info.armado.ausleihe.database.util.OrCondition;
import info.armado.ausleihe.database.util.StringCondition;

/**
 * This class contains all Database access methods to work with games
 * 
 * @author Arndt
 *
 */
@RequestScoped
public class GamesDAO extends EntityDao<Game, Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GamesDAO() {
		super(Game.class);
	}

	/**
	 * This method returns all games from the given list, that already exist in
	 * the database. All other games are not returned.
	 * 
	 * @param games
	 *            A list of games which shall be checked.
	 * @return A list of games which are also contained in the given games list
	 *         and in the database
	 */
	@Transactional
	public Set<Barcode> selectAllGamesAlreadyPersisted(List<Game> games) {
		List<Barcode> barcodes = games.stream().map(game -> game.getBarcode()).collect(Collectors.toList());

		Set<Barcode> result = barcodes.stream().filter(this::exists).collect(Collectors.toSet());

		return result;
	}

	/**
	 * This method returns all games that are available for Darmstadt Spielt
	 * 
	 * @return
	 */
	public List<Game> selectAllAvailableGames() {
		return em.createQuery("from Game g where g.available = true", Game.class).getResultList();
	}

	/**
	 * This method returns all games from the given organizer in the database
	 * 
	 * @param organizer
	 *            The organizer who owns the games
	 * @return A list of all games owned by the given organizer
	 */
	public List<Game> selectAllFrom(Prefix organizer) {
		String query = "from Game g where g.barcode like :barcode";

		return em.createQuery(query, Game.class).setParameter("barcode", Barcode.createWildcard(organizer))
				.getResultList();
	}

	/**
	 * This methods returns all currently lend games from the given organizer in
	 * the database
	 * 
	 * @param organizer
	 *            The organizer who owns the lend games
	 * @return A list of all lend games provided by the given organizer
	 */
	public List<Game> selectAllLendFrom(Prefix organizer) {
		String query = "select lg.game from LendGame lg where lg.game.barcode like :barcode and lg.returnTime is null";

		return em.createQuery(query, Game.class).setParameter("barcode", Barcode.createWildcard(organizer))
				.getResultList();
	}

	/**
	 * This method returns all lend games in the database
	 * 
	 * @return A list of all lend games
	 */
	public List<Game> selectAllLend() {
		return em.createQuery("select lg.game from LendGame lg where lg.returnTime is null", Game.class)
				.getResultList();
	}

	/**
	 * This method returns a GameInfo object for all different games in the
	 * database. Depending on the mustBeAvailable parameter only the activated
	 * games are collected.
	 * 
	 * @param mustBeAvailable
	 *            True if only the available games are needed
	 * @return A list of GameInfo objects, one object for each available
	 *         gametype
	 */
	public List<GameInfo> selectAllDifferentGames(boolean mustBeAvailable) {
		String query = "";
		if (mustBeAvailable)
			query = "select new info.armado.ausleihe.database.objects.GameInfo(title, count(*)) from Game game where game.available = true group by game.title";
		else
			query = "select new info.armado.ausleihe.database.objects.GameInfo(title, count(*)) from Game game group by game.title";

		return em.createQuery(query, GameInfo.class).getResultList();
	}

	/**
	 * This method returns all games (available or not available) with the given
	 * title.
	 * 
	 * @param title
	 *            The title of the searched games
	 * @return A list of all games in the database with the given title
	 */
	public List<Game> selectAllGamesWithTitle(String title) {
		return em.createQuery("from Game game where game.title = :title", Game.class).setParameter("title", title)
				.getResultList();
	}

	public List<Game> selectAllGamesWithRequirements(String searchTerm, String title, String author, String publisher, Integer playerCount, Integer playerAge, Integer gameDuration) {
		StringBuilder sb = new StringBuilder("from Game game");
		
		AndCondition whereClause = new AndCondition();
		
		if (title != null) {
			whereClause.addCondition(new StringCondition("game.title like :title"));
		}

		if (author != null) {
			whereClause.addCondition(new StringCondition("game.author like :author"));
		}
		
		if (publisher != null) {
			whereClause.addCondition(new StringCondition("game.publisher like :publisher"));
		}
		
		if (searchTerm != null) {
			whereClause.addCondition(new OrCondition(
					new StringCondition("game.title like :searchTerm"),
					new StringCondition("game.author like :searchTerm"),
					new StringCondition("game.publisher like :searchTerm")));
		}
		
		if (playerCount != null) {
			whereClause.addCondition(new AndCondition(
					new StringCondition("game.playerCount.minPlayerCount <= :playerCount"),
					new StringCondition("game.playerCount.maxPlayerCount >= :playerCount")));
		}
		
		if (playerAge != null) {
			whereClause.addCondition(new StringCondition("game.minimumAge <= :playerAge"));
		}
		
		if (gameDuration != null) {
			whereClause.addCondition(new AndCondition(
					new StringCondition("game.gameDuration.minDuration <= :gameDuration"),
					new StringCondition("game.gameDuration.maxDuration >= :gameDuration")));
		}
		
		if (!whereClause.isEmpty()) {
			sb.append(" where ").append(whereClause.toString());
		}
		
		TypedQuery<Game> query = em.createQuery(sb.toString(), Game.class);

		if (searchTerm != null)
			query.setParameter("searchTerm", "%" + searchTerm + "%");

		if (title != null)
			query.setParameter("title", "%" + title + "%");

		if (author != null)
			query.setParameter("author", "%" + author + "%");

		if (publisher != null)
			query.setParameter("publisher", "%" + publisher + "%");

		if (playerCount != null) {
			query.setParameter("playerCount", playerCount);
		}
		
		if (playerAge != null) {
			query.setParameter("playerAge", playerAge);
		}
		
		if (gameDuration != null) {
			query.setParameter("gameDuration", gameDuration);
		}
		
		return query.getResultList();
	}

	/**
	 * This method returns all games (available or not available) whose title
	 * contains the given title
	 * 
	 * @param title
	 *            The string to be contained in the returned game titles
	 * @return A list of all games whose title contains the given title
	 */
	public List<Game> selectAllGamesContainingTitle(String title) {
		return em.createQuery("from Game game where game.title like :title", Game.class)
				.setParameter("title", "%" + title + "%").getResultList();
	}

	/**
	 * This method returns the number of all available games in the database. A
	 * game is available, if its available attribute is set to true.
	 * 
	 * @return The number of all available games
	 */
	public long selectNumberOfAvailableGames() {
		String query = "select count(*) from Game game where game.available = true";

		return em.createQuery(query, Long.class).getSingleResult();
	}

	/**
	 * This method returns the number of available games in the database, that
	 * belong to the BDKJ. All games belonging to the BDKJ start with a 22
	 * prefix.
	 * 
	 * @return The number of available games belonging to the BDKJ
	 */
	public long selectNumberOfAvailableBDKJGames() {
		String query = "select count(*) from Game game where game.available = true and game.barcode like :barcode";

		return em.createQuery(query, Long.class).setParameter("barcode", Barcode.createWildcard(Prefix.BDKJ))
				.getSingleResult();
	}

	/**
	 * This method returns the number of available games in the database, that
	 * belong to the Spielekreis. All games that belong to the Spielekreis start
	 * with a 11 prefix.
	 * 
	 * @return The number of available games belonging to the Spielekreis
	 */
	public long selectNumberOfAvailableSpielekreisGames() {
		String query = "select count(*) from Game game where game.available = true and game.barcode like :barcode";

		return em.createQuery(query, Long.class).setParameter("barcode", Barcode.createWildcard(Prefix.Spielekreis))
				.getSingleResult();
	}

	@Transactional
	public void deleteAllGamesFrom(Prefix organisator) {
		String query = "delete from Game game where game.barcode like :barcode";

		em.createQuery(query).setParameter("barcode", Barcode.createWildcard(organisator)).executeUpdate();
	}
}
