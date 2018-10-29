package info.armado.ausleihe.database.access

import java.time.Year

import info.armado.ausleihe.database.WebDeployment
import info.armado.ausleihe.database.barcode.Barcode
import info.armado.ausleihe.database.dataobjects.{GameDuration, GameInfo, PlayerCount, Prefix}
import info.armado.ausleihe.database.entities.Game
import javax.inject.Inject
import org.arquillian.ape.rdbms.{ShouldMatchDataSet, UsingDataSet}
import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.{be, convertToAnyShouldWrapper, equal}
import org.scalatest.junit.JUnitSuite

object GamesDaoTest extends WebDeployment

@RunWith(classOf[Arquillian])
class GamesDaoTest extends JUnitSuite {
  @Inject
  var gamesDao: GamesDao = _

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectByBarcode(): Unit = {
    gamesDao.selectByBarcode(Barcode("11000047")) should equal(
      Some(Game(Barcode("11000047"), "Titel 3", "Autor 2", "Verlag 3", PlayerCount(2, 4), GameDuration(90, 120), 12, Year.of(2016), null, true))
    )

    gamesDao.selectByBarcode(Barcode("11000070")) should equal(
      Some(Game(Barcode("11000070"), "Titel 5", "Autor 1", "Verlag 1", PlayerCount(1, 3), GameDuration(90, 120), 12, Year.of(2016), null, true))
    )

    gamesDao.selectByBarcode(Barcode("33000010")) should equal(
      None
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectActivatedByBarcode(): Unit = {
    gamesDao.selectActivatedByBarcode(Barcode("11000047")) should equal(
      Some(Game(Barcode("11000047"), "Titel 3", "Autor 2", "Verlag 3", PlayerCount(2, 4), GameDuration(90, 120), 12, Year.of(2016), null, true))
    )

    gamesDao.selectActivatedByBarcode(Barcode("11000070")) should equal(
      None
    )

    gamesDao.selectActivatedByBarcode(Barcode("33000010")) should equal(
      None
    )
  }

  @Test
  @UsingDataSet(Array("datasets/empty.xml"))
  @ShouldMatchDataSet(value = Array("datasets/insert-games.xml"), excludeColumns = Array("GAME.ID"))
  def insertGame(): Unit = {
    gamesDao.insert(Game(Barcode("11000014"), "Title 1", "Author 1", "Publisher 1", true))
    gamesDao.insert(Game(Barcode("11000025"), "Title 2", "Author 2", "Publisher 2", false))
    gamesDao.insert(Game(Barcode("11000036"), "Title 3", "Author 3", "Publisher 3", PlayerCount(2, 5), null, 10, Year.of(2017), "Comment 1", true))
    gamesDao.insert(Game(Barcode("11000058"), "Title 4", "Author 4", "Publisher 4", PlayerCount(3, 7), GameDuration(60), 11, null, null, true))
    gamesDao.insert(Game(Barcode("11000069"), "Title 5", "Author 5", "Publisher 5", PlayerCount(3), GameDuration(30, 90), 12, Year.of(2016), "Comment 3", false))
    gamesDao.insert(Game(Barcode("11000081"), "Title 6", "Author 6", "Publisher 6", null, GameDuration(10, 30), null, Year.of(2015), "Comment 4", true))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectAllLend(): Unit = {
    gamesDao.selectAllLend().toSet should be(Set(
      Game(Barcode("11000047"), "Titel 3", "Autor 2", "Verlag 3", PlayerCount(2, 4), GameDuration(90, 120), 12, Year.of(2016), null, true),
      Game(Barcode("11000014"), "Titel 1", "Autor 1", "Verlag 1", PlayerCount(2, 2), GameDuration(90, 120), 12, Year.of(2016), null, true),
      Game(Barcode("11000025"), "Titel 2", "Autor 1", "Verlag 2", null, GameDuration(90, 120), 15, Year.of(2016), null, true),
      Game(Barcode("11000036"), "Titel 2", "Autor 1", "Verlag 2", null, GameDuration(90, 120), 15, Year.of(2016), null, true)
    ))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectAllLendFrom(): Unit = {
    gamesDao.selectAllLendFrom(Prefix.BDKJ).size should be(0)
    gamesDao.selectAllLendFrom(Prefix.Spielekreis).toSet should equal(Set(
      Game(Barcode("11000047"), "Titel 3", "Autor 2", "Verlag 3", PlayerCount(2, 4), GameDuration(90, 120), 12, Year.of(2016), null, true),
      Game(Barcode("11000014"), "Titel 1", "Autor 1", "Verlag 1", PlayerCount(2, 2), GameDuration(90, 120), 12, Year.of(2016), null, true),
      Game(Barcode("11000025"), "Titel 2", "Autor 1", "Verlag 2", null, GameDuration(90, 120), 15, Year.of(2016), null, true),
      Game(Barcode("11000036"), "Titel 2", "Autor 1", "Verlag 2", null, GameDuration(90, 120), 15, Year.of(2016), null, true)
    ))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectAllDifferentGames(): Unit = {
    val allGames = gamesDao.selectAllDifferentGames(false)

    allGames.size should be(5)
    allGames.toSet should equal(Set(GameInfo("Titel 1", 1), GameInfo("Titel 2", 2), GameInfo("Titel 3", 1), GameInfo("Titel 4", 2), GameInfo("Titel 5", 1)))

    val availableGames = gamesDao.selectAllDifferentGames(true)

    availableGames.size should be(4)
    availableGames.toSet should equal(Set(GameInfo("Titel 1", 1), GameInfo("Titel 2", 2), GameInfo("Titel 3", 1), GameInfo("Titel 4", 2)))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectAllGamesContainingTitle(): Unit = {
    gamesDao.selectAllGamesContainingTitle("Titel 2").toSet should equal(Set(
      Game(Barcode("11000025"), "Titel 2", "Autor 1", "Verlag 2", null, GameDuration(90, 120), 15, Year.of(2016), null, true),
      Game(Barcode("11000036"), "Titel 2", "Autor 1", "Verlag 2", null, GameDuration(90, 120), 15, Year.of(2016), null, true)
    ))
  }

  @Test
  @UsingDataSet(Array("datasets/bdkj-spielekreis-games.xml"))
  @ShouldMatchDataSet(Array("datasets/bdkj-spielekreis-games.xml"))
  def selectAllFrom(): Unit = {
    gamesDao.selectAllFrom(Prefix.BDKJ).toSet should equal(Set(
      Game(Barcode("22000017"), "Title 3", "Author 3", "Publisher 3", PlayerCount(2, 5), null, 10, Year.of(2017), "Comment 1", true),
      Game(Barcode("22000028"), "Title 5", "Author 5", "Publisher 5", PlayerCount(3, 3), GameDuration(30, 90), 12, Year.of(2016), "Comment 3", false)
    ))
    gamesDao.selectAllFrom(Prefix.Spielekreis).toSet should equal(Set(
      Game(Barcode("11000014"), "Title 1", "Author 1", "Publisher 1", true),
      Game(Barcode("11000025"), "Title 2", "Author 2", "Publisher 2", false),
      Game(Barcode("11000058"), "Title 4", "Author 4", "Publisher 4", PlayerCount(3, 7), GameDuration(60, 60), 11, null, null, true),
      Game(Barcode("11000081"), "Title 6", "Author 6", "Publisher 6", null, GameDuration(10, 30), null, Year.of(2015), "Comment 4", true)
    ))
  }

  @Test
  @UsingDataSet(Array("datasets/bdkj-spielekreis-games.xml"))
  @ShouldMatchDataSet(Array("datasets/bdkj-spielekreis-games.xml"))
  def selectNumberOfAvailableGamesFrom(): Unit = {
    gamesDao.selectNumberOfAvailableGamesFrom(Prefix.BDKJ) should be(1)
    gamesDao.selectNumberOfAvailableGamesFrom(Prefix.Spielekreis) should be(3)
  }

  @Test
  @UsingDataSet(Array("datasets/insert-games.xml"))
  @ShouldMatchDataSet(Array("datasets/insert-games.xml"))
  def selectAllAvailableGames(): Unit = {
    gamesDao.selectAllAvailable().toSet should equal(Set(
      Game(Barcode("11000014"), "Title 1", "Author 1", "Publisher 1", true),
      Game(Barcode("11000036"), "Title 3", "Author 3", "Publisher 3", PlayerCount(2, 5), null, 10, Year.of(2017), "Comment 1", true),
      Game(Barcode("11000058"), "Title 4", "Author 4", "Publisher 4", PlayerCount(3, 7), GameDuration(60), 11, null, null, true),
      Game(Barcode("11000081"), "Title 6", "Author 6", "Publisher 6", null, GameDuration(10, 30), null, Year.of(2015), "Comment 4", true)
    ))
  }

  @Test
  @UsingDataSet(Array("datasets/bdkj-spielekreis-games.xml"))
  @ShouldMatchDataSet(Array("datasets/only-spielekreis-games.xml"))
  def deleteAllGamesFrom(): Unit = {
    gamesDao.deleteAllGamesFrom(Prefix.BDKJ)

    gamesDao.selectAll().size should be(4)
    gamesDao.selectAllFrom(Prefix.BDKJ).size should be(0)
    gamesDao.selectAllFrom(Prefix.Spielekreis).size should be(4)
  }
}