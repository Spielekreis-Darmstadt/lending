package info.armado.ausleihe.database.access

import java.lang.{Long => JLong}

import info.armado.ausleihe.database.barcode.Barcode
import info.armado.ausleihe.database.dataobjects.{GameInfo, Prefix}
import info.armado.ausleihe.database.entities.Game
import info.armado.ausleihe.database.util.{AndCondition, OrCondition, StringCondition}
import javax.persistence.TypedQuery
import javax.transaction.Transactional

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.mutable

/**
  * A data access object for accessing [[Game]] objects from a database
  *
  * @author Marc Arndt
  */
class GamesDao extends EntityDao[Game, Integer](classOf[Game]) {
  /**
    * Queries a list of all games from a given organizer inside the database
    *
    * @param organizer The organizer, who owns the games
    * @return A list of all games owned by the given organizer
    */
  def selectAllFrom(organizer: Prefix): List[Game] =
    em.createQuery("from Game g where g.barcode like :barcode", classOf[Game])
      .setParameter("barcode", Barcode.createWildcard(organizer)).getResultList.asScala.toList

  /**
    * Queries all currently lend games from a given organizer from the database
    *
    * @param organizer The organizer who owns the games
    * @return A list of all lend games provided by the given organizer
    */
  def selectAllLendFrom(organizer: Prefix): List[Game] =
    em.createQuery("select lg.game from LendGame lg where lg.game.barcode like :barcode and lg.returnTime is null", classOf[Game])
      .setParameter("barcode", Barcode.createWildcard(organizer)).getResultList.asScala.toList


  /**
    * Queries all currently lend games from the database
    *
    * @return A list of all lend games
    */
  def selectAllLend(): List[Game] = em.createQuery("select lg.game from LendGame lg where lg.returnTime is null", classOf[Game])
    .getResultList.asScala.toList

  /**
    * This method returns a GameInfo object for all different games in the
    * database. Depending on the mustBeAvailable parameter only the activated
    * games are collected.
    *
    * @param mustBeAvailable True if only the available games are needed
    * @return A list of GameInfo objects, one object for each available game type
    */
  def selectAllDifferentGames(mustBeAvailable: Boolean): List[GameInfo] = mustBeAvailable match {
    case true =>
      em.createQuery("select new info.armado.ausleihe.database.dataobjects.GameInfo(title, count(*)) from Game game where game.available = true group by game.title", classOf[GameInfo])
        .getResultList.asScala.toList
    case false =>
      em.createQuery("select new info.armado.ausleihe.database.dataobjects.GameInfo(title, count(*)) from Game game group by game.title", classOf[GameInfo])
        .getResultList.asScala.toList
  }

  /**
    * This method returns all games (available or not available) with the given title.
    *
    * @param title The title of the searched games
    * @return A list of all games in the database with the given title
    */
  def selectAllGamesWithTitle(title: String): List[Game] =
    em.createQuery("from Game game where game.title = :title", classOf[Game])
      .setParameter("title", title).getResultList.asScala.toList

  /**
    * Queries a list of games who all fulfill the requirements by the given parameters
    *
    * @param searchTerm   An optional search term, the title, author or publisher needs to contain
    * @param title        An optional title
    * @param author       An optional author
    * @param publisher    An optional publisher
    * @param playerCount  An optional number of players
    * @param playerAge    An optional minimum player age
    * @param gameDuration An optional desired game duration
    * @return A list of all games fulfilling the given requirements
    */
  def selectAllGamesWithRequirements(searchTerm: Option[String], title: Option[String], author: Option[String],
                                     publisher: Option[String], playerCount: Option[Integer], playerAge: Option[Integer],
                                     gameDuration: Option[Integer]): List[Game] = {
    val whereClause = new AndCondition()

    title.foreach(x => whereClause + StringCondition("game.title like :title"))
    author.foreach(x => whereClause + StringCondition("game.author like :author"))
    publisher.foreach(x => whereClause + StringCondition("game.publisher like :publisher"))
    searchTerm.foreach(x => whereClause + OrCondition(mutable.MutableList(StringCondition("game.title like :searchTerm"), StringCondition("game.author like :searchTerm"), StringCondition("game.publisher like :searchTerm"))))
    playerCount.foreach(x => whereClause + AndCondition(mutable.MutableList(StringCondition("game.playerCount.minPlayerCount <= :playerCount"), StringCondition("game.playerCount.maxPlayerCount >= :playerCount"))))
    playerAge.foreach(x => whereClause + StringCondition("game.minimumAge <= :playerAge"))
    gameDuration.foreach(x => whereClause + AndCondition(mutable.MutableList(StringCondition("game.gameDuration.minDuration <= :gameDuration"), StringCondition("game.gameDuration.maxDuration >= :gameDuration"))))

    val sb = new StringBuilder("from Game game")
    if (!whereClause.isEmpty) {
      sb.append(" where ")
      sb.append(whereClause.toQueryString)
    }

    val query: TypedQuery[Game] = em.createQuery(sb.toString, classOf[Game])

    searchTerm.foreach(searchTerm => query.setParameter("searchTerm", s"%$searchTerm%"))
    title.foreach(title => query.setParameter("title", s"%$title%"))
    author.foreach(author => query.setParameter("author", s"%$author%"))
    publisher.foreach(publisher => query.setParameter("publisher", s"%$publisher%"))
    playerCount.foreach(playerCount => query.setParameter("playerCount", playerCount))
    playerAge.foreach(playerAge => query.setParameter("playerAge", playerAge))
    gameDuration.foreach(gameDuration => query.setParameter("gameDuration", gameDuration))

    query.getResultList.asScala.toList
  }

  /**
    * Queries all games from the database (independent of their availability) whose title contains a given title string
    *
    * @param title The string the game title needs to contain
    * @return A list of all games whose title contains the given string
    */
  def selectAllGamesContainingTitle(title: String): List[Game] =
    em.createQuery("from Game game where game.title like :title", classOf[Game])
      .setParameter("title", s"%$title%").getResultList.asScala.toList

  /**
    * Queries the number of available games from a given organizer.
    *
    * @param organizer The prefix of the organizer whose number of available games is queried
    * @return The number of available games
    */
  def selectNumberOfAvailableGamesFrom(organizer: Prefix): Long =
    em.createQuery("select count(*) from Game game where game.available = true and game.barcode like :barcode", classOf[JLong])
      .setParameter("barcode", Barcode.createWildcard(organizer)).getSingleResult

  /**
    * Deletes all games from a given organizer inside the database
    *
    * @param organizer The organizer whose games should be deleted
    */
  @Transactional
  def deleteAllGamesFrom(organizer: Prefix): Unit =
    em.createQuery("delete from Game game where game.barcode like :barcode")
      .setParameter("barcode", Barcode.createWildcard(organizer)).executeUpdate
}
