package info.armado.ausleihe.database

import java.time.{LocalDateTime, Year}

import info.armado.ausleihe.database.access._
import info.armado.ausleihe.database.barcode.Barcode
import info.armado.ausleihe.database.dataobjects.{GameDuration, PlayerCount}
import info.armado.ausleihe.database.entities._
import javax.inject.Inject
import org.arquillian.ape.rdbms.{ShouldMatchDataSet, UsingDataSet}
import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.{convertToAnyShouldWrapper, equal}
import org.scalatest.junit.JUnitSuite

object LendGameDaoTest extends WebDeployment

@RunWith(classOf[Arquillian])
class LendGameDaoTest extends JUnitSuite {
  @Inject
  var lendGameDao: LendGameDao = _

  @Inject
  var lendIdentityCardDao: LendIdentityCardDao = _

  @Inject
  var identityCardDao: IdentityCardDao = _

  @Inject
  var gamesDao: GamesDao = _

  @Inject
  var envelopeDao: EnvelopeDao = _

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def isGameLend(): Unit = {
    gamesDao.selectByBarcode(Barcode("11000014")).foreach(
      game => lendGameDao.isGameLend(game) should equal(true)
    )

    gamesDao.selectByBarcode(Barcode("11000036")).foreach(
      game => lendGameDao.isGameLend(game) should equal(true)
    )

    gamesDao.selectByBarcode(Barcode("11000058")).foreach(
      game => lendGameDao.isGameLend(game) should equal(false)
    )

    gamesDao.selectByBarcode(Barcode("11000070")).foreach(
      game => lendGameDao.isGameLend(game) should equal(false)
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectLendGameByGame(): Unit = {
    gamesDao.selectByBarcode(Barcode("11000014")).foreach(
      game => lendGameDao.selectLendGameByGame(game) should equal(
        Some(
          LendGame(game,
            LendIdentityCard(IdentityCard(Barcode("33000010"), true), Envelope(Barcode("44000013"), true), LocalDateTime.parse("2016-11-05T10:00:00.0"), "Marc Arndt"),
            LocalDateTime.parse("2016-11-05T10:05:00.0"))))
    )

    gamesDao.selectByBarcode(Barcode("11000036")).foreach(
      game => lendGameDao.selectLendGameByGame(game) should equal(
        Some(
          LendGame(game,
            LendIdentityCard(IdentityCard(Barcode("33000010"), true), Envelope(Barcode("44000013"), true), LocalDateTime.parse("2016-11-05T10:00:00.0"), "Marc Arndt"),
            LocalDateTime.parse("2016-11-05T10:05:00.0")))
      )
    )

    gamesDao.selectByBarcode(Barcode("11000058")).foreach(
      game => lendGameDao.selectLendGameByGame(game) should equal(
        None
      )
    )

    gamesDao.selectByBarcode(Barcode("11000070")).foreach(
      game => lendGameDao.selectLendGameByGame(game) should equal(
        None
      )
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectLendGameByGameBarcode(): Unit = {
    gamesDao.selectByBarcode(Barcode("11000014")).foreach(
      game => lendGameDao.selectLendGameByGameBarcode(Barcode("11000014")) should equal(
        Some(
          LendGame(game,
            LendIdentityCard(IdentityCard(Barcode("33000010"), true), Envelope(Barcode("44000013"), true), LocalDateTime.parse("2016-11-05T10:00:00.0"), "Marc Arndt"),
            LocalDateTime.parse("2016-11-05T10:05:00.0"))))
    )

    gamesDao.selectByBarcode(Barcode("11000036")).foreach(
      game => lendGameDao.selectLendGameByGameBarcode(Barcode("11000036")) should equal(
        Some(
          LendGame(game,
            LendIdentityCard(IdentityCard(Barcode("33000010"), true), Envelope(Barcode("44000013"), true), LocalDateTime.parse("2016-11-05T10:00:00.0"), "Marc Arndt"),
            LocalDateTime.parse("2016-11-05T10:05:00.0")))
      )
    )

    lendGameDao.selectLendGameByGameBarcode(Barcode("11000058")) should equal(
      None
    )

    lendGameDao.selectLendGameByGameBarcode(Barcode("11000070")) should equal(
      None
    )

  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectCurrentLendIdentityCardByGame(): Unit = {
    gamesDao.selectByBarcode(Barcode("11000014")).foreach(
      game => lendGameDao.selectCurrentLendIdentityCardByGame(game) should equal(
        Some(LendIdentityCard(IdentityCard(Barcode("33000010"), true), Envelope(Barcode("44000013"), true), LocalDateTime.parse("2016-11-05T10:00:00.0"), "Marc Arndt"))
      )
    )

    gamesDao.selectByBarcode(Barcode("11000036")).foreach(
      game => lendGameDao.selectCurrentLendIdentityCardByGame(game) should equal(
        Some(LendIdentityCard(IdentityCard(Barcode("33000010"), true), Envelope(Barcode("44000013"), true), LocalDateTime.parse("2016-11-05T10:00:00.0"), "Marc Arndt"))
      )
    )

    gamesDao.selectByBarcode(Barcode("11000058")).foreach(
      game => lendGameDao.selectCurrentLendIdentityCardByGame(game) should equal(
        None
      )
    )

    gamesDao.selectByBarcode(Barcode("11000070")).foreach(
      game => lendGameDao.selectCurrentLendIdentityCardByGame(game) should equal(
        None
      )
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def hasLendIdentityCardCurrentlyLendGames(): Unit = {
    lendIdentityCardDao.selectCurrentByIdentityCardBarcode(Barcode("33000010")).foreach(
      lendIdentityCard => lendGameDao.hasLendIdentityCardCurrentlyLendGames(lendIdentityCard) should equal(true)
    )

    lendIdentityCardDao.selectCurrentByIdentityCardBarcode(Barcode("33000021")).foreach(
      lendIdentityCard => lendGameDao.hasLendIdentityCardCurrentlyLendGames(lendIdentityCard) should equal(true)
    )

    lendIdentityCardDao.selectCurrentByIdentityCardBarcode(Barcode("33000032")).foreach(
      lendIdentityCard => lendGameDao.hasLendIdentityCardCurrentlyLendGames(lendIdentityCard) should equal(false)
    )

    lendIdentityCardDao.selectCurrentByIdentityCardBarcode(Barcode("33000043")).foreach(
      lendIdentityCard => lendGameDao.hasLendIdentityCardCurrentlyLendGames(lendIdentityCard) should equal(false)
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectCurrentLendGamesByLendIdentityCard(): Unit = {
    lendIdentityCardDao.selectCurrentByIdentityCardBarcode(Barcode("33000010")).foreach(
      lendIdentityCard => lendGameDao.selectCurrentLendGamesByLendIdentityCard(lendIdentityCard).toSet should equal(
        Set(
          LendGame(Game(Barcode("11000014"), "Titel 1", "Autor 1", "Verlag 1", PlayerCount(2, 2), GameDuration(90, 120), 12, Year.of(2016), null, true),
            LendIdentityCard(IdentityCard(Barcode("33000010"), true), Envelope(Barcode("44000013"), true), LocalDateTime.parse("2016-11-05T10:00:00.0"), "Marc Arndt"),
            LocalDateTime.parse("2016-11-05T10:05:00.0")),
          LendGame(Game(Barcode("11000025"), "Titel 2", "Autor 1", "Verlag 2", null, GameDuration(90, 120), 15, Year.of(2016), null, true),
            LendIdentityCard(IdentityCard(Barcode("33000010"), true), Envelope(Barcode("44000013"), true), LocalDateTime.parse("2016-11-05T10:00:00.0"), "Marc Arndt"),
            LocalDateTime.parse("2016-11-05T10:05:00.0")),
          LendGame(Game(Barcode("11000036"), "Titel 2", "Autor 1", "Verlag 2", null, GameDuration(90, 120), 15, Year.of(2016), null, true),
            LendIdentityCard(IdentityCard(Barcode("33000010"), true), Envelope(Barcode("44000013"), true), LocalDateTime.parse("2016-11-05T10:00:00.0"), "Marc Arndt"),
            LocalDateTime.parse("2016-11-05T10:05:00.0"))
        )
      )
    )

    lendIdentityCardDao.selectCurrentByIdentityCardBarcode(Barcode("33000021")).foreach(
      Set(
        LendGame(Game(Barcode("11000047"), "Titel 3", "Autor 2", "Verlag 3", PlayerCount(2, 4), GameDuration(90, 120), 12, Year.of(2016), null, true),
          LendIdentityCard(IdentityCard(Barcode("33000021"), true), Envelope(Barcode("44000024"), true), LocalDateTime.parse("2016-11-05T10:00:00.0")),
          LocalDateTime.parse("2016-11-05T10:05:00.0")),
      )
    )

    lendIdentityCardDao.selectCurrentByIdentityCardBarcode(Barcode("33000032")).foreach(
      lendIdentityCard => lendGameDao.selectCurrentLendGamesByLendIdentityCard(lendIdentityCard) should equal(None)
    )

    lendIdentityCardDao.selectCurrentByIdentityCardBarcode(Barcode("33000043")).foreach(
      lendIdentityCard => lendGameDao.selectCurrentLendGamesByLendIdentityCard(lendIdentityCard) should equal(None)
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectNumberOfCurrentLendBDKJGames(): Unit = {
    lendGameDao.selectNumberOfCurrentLendSpielekreisGames should equal(4)
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def selectNumberOfCurrentLendSpielekreisGames(): Unit = {
    lendGameDao.selectNumberOfCurrentLendBDKJGames should equal(0)
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(value = Array("datasets/lend-game.xml"), excludeColumns = Array("LENDGAME.ID", "LENDGAME.LENDTIME"))
  def issueGames(): Unit = {
    (gamesDao.selectByBarcode(Barcode("11000069")), lendIdentityCardDao.selectCurrentByIdentityCardBarcode(Barcode("33000101"))) match {
      case (Some(game), Some(lendIdentityCard)) => {
        lendGameDao.isGameLend(game) should equal(false)

        lendGameDao.issueGames(List(game), lendIdentityCard)

        lendGameDao.isGameLend(game) should equal(true)
      }
      case _ => fail("Default case should't occur")
    }
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(value = Array("datasets/return-game.xml"), excludeColumns = Array("LENDGAME.ID", "LENDGAME.RETURNTIME"))
  def returnGame(): Unit = {
    gamesDao.selectByBarcode(Barcode("11000047")).foreach(
      game => {
        lendGameDao.isGameLend(game) should equal(true)

        lendGameDao.returnGame(game)

        lendGameDao.isGameLend(game) should equal(false)
      }
    )
  }
}
