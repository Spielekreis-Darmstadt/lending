package info.armado.ausleihe.database

import java.time.Year
import javax.inject.Inject

import info.armado.ausleihe.database.access.GamesDAO
import info.armado.ausleihe.database.barcode.Barcode
import info.armado.ausleihe.database.dataobjects.{GameDuration, PlayerCount}
import info.armado.ausleihe.database.entities._
import org.arquillian.ape.rdbms.{ShouldMatchDataSet, UsingDataSet}
import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitSuite
import org.scalatest.Matchers.{be, convertToAnyShouldWrapper, equal}

object GamesDaoTest extends WebDeployment

@RunWith(classOf[Arquillian])
class GamesDaoTest extends JUnitSuite {
  @Inject
  var gamesDao: GamesDAO = _

  @Test
  @UsingDataSet(Array("datasets/empty.xml"))
  @ShouldMatchDataSet(Array("datasets/insert-games.xml"))
  def insertGame(): Unit = {
    gamesDao.insert(Game(Barcode("11000014"), "Title 1", "Author 1", "Publisher 1", true))
    gamesDao.insert(Game(Barcode("11000025"), "Title 2", "Author 2", "Publisher 2", false))
    gamesDao.insert(Game(Barcode("11000036"), "Title 3", "Author 3", "Publisher 3", PlayerCount(2, 5), null, 10, Year.of(2017), "Comment 1", true))
    gamesDao.insert(Game(Barcode("11000058"), "Title 4", "Author 4", "Publisher 4", PlayerCount(3, 7), GameDuration(60), 11, null, null, true))
    gamesDao.insert(Game(Barcode("11000069"), "Title 5", "Author 5", "Publisher 5", PlayerCount(3), GameDuration(30, 90), 12, Year.of(2016), "Comment 3", false))
    gamesDao.insert(Game(Barcode("11000081"), "Title 6", "Author 6", "Publisher 6", null, GameDuration(10, 30), null, Year.of(2015), "Comment 4", true))
  }

  def selectAllAvailableGames(): Unit = {
    gamesDao.selectAllAvailableGames()
  }
}