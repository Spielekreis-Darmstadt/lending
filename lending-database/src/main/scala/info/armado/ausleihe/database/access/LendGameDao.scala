package info.armado.ausleihe.database.access

import java.lang.{Long => JLong}
import java.time.LocalDateTime

import info.armado.ausleihe.database.barcode.Barcode
import info.armado.ausleihe.database.dataobjects.Prefix
import info.armado.ausleihe.database.entities.{Game, LendGame, LendIdentityCard}
import javax.transaction.Transactional

import scala.collection.JavaConverters.asScalaBufferConverter
import scala.util.Try

/**
  * A data access object for accessing [[LendGame]] instances from a database
  *
  * @author Marc Arndt
  */
class LendGameDao extends LendEntityDao[LendGame, Integer](classOf[LendGame]) {
  /**
    * This method checks if a given game is currently borrowed.
    *
    * @param game The game to be checked if it's currently borrowed
    * @return True if the given game is currently borrowed, false otherwise
    */
  def isGameLend(game: Game): Boolean = {
    val count = em
      .createQuery("select count(*) from LendGame lg where lg.game = :game and lg.returnTime is null", classOf[JLong])
      .setParameter("game", game).getSingleResult

    count == 1
  }

  /**
    * This method returns the current LendGame object corresponding to the given Game.
    * The current LendGame object has a returnTime of null.
    * If no such object exists it means, that the given Game is currently not lent.
    *
    * @param game The game whose LendGame object is wanted
    * @return The current LendGame object belonging to the given Game object, or null if no such LendGame object exists
    */
  def selectLendGameByGame(game: Game): Option[LendGame] =
    Try(
      em.createQuery("from LendGame lg where lg.game = :game and lg.returnTime is null", classOf[LendGame])
        .setParameter("game", game).getSingleResult
    ).toOption

  /**
    * This method returns the current LendGame object corresponding to a game with the given barcode with a returnTime of null.
    * If no such object exists it means, that the given Game is either currently not lent or doesn't exist.
    *
    * @param barcode The barcode of a game whose current LendGame object is searched
    * @return
    */
  def selectLendGameByGameBarcode(barcode: Barcode): Option[LendGame] =
    Try(
      em.createQuery("from LendGame lg where lg.game.barcode = :barcode and lg.returnTime is null", classOf[LendGame])
        .setParameter("barcode", barcode).getSingleResult
    ).toOption

  /**
    * This method returns the current LendIdentityCard object for a given game.
    * The current LendIdentityCard belongs to a LendGame object with a returnTime of null.
    * If no such object exists it means, that the given
    * game is currently not lent.
    *
    * @param game The game whose current LendIdentityCard is wanted
    * @return The current LendIdentityCard belonging to the given Game, or null if no such LendIdentityCard exists
    */
  def selectCurrentLendIdentityCardByGame(game: Game): Option[LendIdentityCard] =
    Try(
      em.createQuery("select lg.lendIdentityCard from LendGame lg where lg.game = :game and lg.returnTime is null", classOf[LendIdentityCard])
        .setParameter("game", game).getSingleResult
    ).toOption

  /**
    * This method checks, if the given LendIdentityCard has currently borrowed
    * one or more games.
    *
    * @param lic The LendIdentityCard which should be checked for having borrowed games
    * @return True, if the given LendIdentityCard has currently borrowed one or more games
    */
  def hasLendIdentityCardCurrentlyLendGames(lic: LendIdentityCard): Boolean = {
    val count: Long = em
      .createQuery("select count(*) from LendGame lg where lg.lendIdentityCard = :lic and lg.returnTime is null", classOf[JLong])
      .setParameter("lic", lic).getSingleResult

    count > 0
  }

  /**
    * This method returns all currently borrowed games by the given LendIdentityCard.
    * If the given LendIdentityCard has currently borrowed
    * zero games an empty list is returned
    *
    * @param lic The LendIdentityCard for which the currently borrowed games are requested
    * @return A list of currently borrowed games by the given LendIdentityCard
    */
  def selectCurrentLendGamesByLendIdentityCard(lic: LendIdentityCard): List[LendGame] =
    em.createQuery("from LendGame lg where lg.lendIdentityCard = :lic and lg.returnTime is null", classOf[LendGame])
      .setParameter("lic", lic).getResultList.asScala.toList

  /**
    * This method returns the number of currently lend games provided by the BDKJ.
    * The games from BDKJ all start with the prefix 22.
    *
    * @return The number of currently lend games from BDKJ
    */
  def selectNumberOfCurrentLendBDKJGames: Long =
    em.createQuery("select count(*) from LendGame lg where lg.returnTime is null and lg.game.barcode like :barcode", classOf[JLong])
      .setParameter("barcode", Barcode.createWildcard(Prefix.BDKJ)).getSingleResult

  /**
    * This method returns the number of currently lend games from the Spielekreis.
    * The games from Spielekreis all start with the prefix 11.
    *
    * @return The number of currently lend games from Spielekreis
    */
  def selectNumberOfCurrentLendSpielekreisGames: Long =
    em.createQuery("select count(*) from LendGame lg where lg.returnTime is null and lg.game.barcode like :barcode", classOf[JLong])
      .setParameter("barcode", Barcode.createWildcard(Prefix.Spielekreis)).getSingleResult

  /**
    * This method issues all games in the given games-list to the given
    * LendIdentityCard.
    *
    * @param games An array of games to be lent
    * @param lic   The identity card that should be assigned to the given games
    */
  @Transactional
  def issueGames(games: List[Game], lic: LendIdentityCard): Unit =
    insert(games.map(LendGame(_, lic, LocalDateTime.now)))

  /**
    * This method sets the given game as returned Therefore this method must
    * receive a currently borrowed game, otherwise it will throw an error.
    *
    * @param game The game to be returned
    */
  @Transactional
  def returnGame(game: Game): Unit = selectLendGameByGame(game).foreach(returnGame)

  /**
    * Sets a given [[LendGame]] object as returned
    *
    * @param lendGame The [[LendGame]] to be set as returned
    */
  @Transactional
  def returnGame(lendGame: LendGame): Unit = {
    lendGame.returnTime = LocalDateTime.now

    update(lendGame)
  }
}
