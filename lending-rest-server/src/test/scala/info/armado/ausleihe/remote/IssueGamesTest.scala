package info.armado.ausleihe.remote

import org.jboss.arquillian.junit.Arquillian
import org.jboss.arquillian.persistence.UsingDataSet
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.convertToAnyShouldWrapper
import org.scalatest.Matchers.equal
import org.scalatest.junit.JUnitSuite

import info.armado.ausleihe.remote.dataobjects.entities.GameData
import info.armado.ausleihe.remote.dataobjects.entities.IdentityCardData
import info.armado.ausleihe.remote.requests.IssueGameRequest
import info.armado.ausleihe.remote.results.IssueGamesSuccess
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import info.armado.ausleihe.remote.dataobjects.inuse.GameInUse
import info.armado.ausleihe.remote.dataobjects.entities.EnvelopeData
import info.armado.ausleihe.remote.results.LendingEntityInUse
import info.armado.ausleihe.remote.results.IdentityCardHasIssuedGames
import info.armado.ausleihe.remote.dataobjects.inuse.NotInUse
import info.armado.ausleihe.remote.results.LendingEntityNotExists
import info.armado.ausleihe.remote.results.IncorrectBarcode

object IssueGamesTest extends WebDeployment

@RunWith(classOf[Arquillian])
class IssueGamesTest extends JUnitSuite {
  @Inject
  var issueGamesService: IssueGames = _

  /**
   * Checks that a currently not borrowed game can be lend to an identity card that has currently borrowed at least one game in unlimited mode.
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def successfulUnlimitedIssueGames(): Unit = {
    issueGamesService.issueGames(IssueGameRequest("33000010", Array("11000058"), false)) should equal(IssueGamesSuccess(IdentityCardData("33000010", "Marc Arndt"),
      Array(GameData("11000058", "Titel 4", "Autor 3", "Verlag 2", "13", "3 - 5", "90 - 120"))))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def successfulLimitedIssueGames(): Unit = {
    issueGamesService.issueGames(IssueGameRequest("33000101", Array("11000058"), true)) should equal(IssueGamesSuccess(IdentityCardData("33000101"),
      Array(GameData("11000058", "Titel 4", "Autor 3", "Verlag 2", "13", "3 - 5", "90 - 120"))))
  }

  /**
   * Checks that an identity card that has currently borrowed at least one game can't borrow another game in limited mode.
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def alreadyIssuedIdentityCardIssueGames(): Unit = {
    issueGamesService.issueGames(IssueGameRequest("33000010", Array("11000058"), true)) should equal(IdentityCardHasIssuedGames(IdentityCardData("33000010", "Marc Arndt"),
      Array(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameData("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"), GameData("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"))))
  }

  /**
   * Checks that an identity card that currently has at least one borrowed game can't borrow an already borrowed game in unlimited mode.
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def alreadyLendUnlimitedIssueGames1(): Unit = {
    issueGamesService.issueGames(IssueGameRequest("33000010", Array("11000014"), false)) should equal(LendingEntityInUse(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"),
      GameInUse(IdentityCardData("33000010", "Marc Arndt"), EnvelopeData("44000013"))))
  }

  /**
   * Checks that an identity card that currently has at least one borrowed game can't borrow an already borrowed game in limited mode.
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def alreadyLendLimitedIssueGames1(): Unit = {
    issueGamesService.issueGames(IssueGameRequest("33000010", Array("11000014"), true)) should equal(LendingEntityInUse(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"),
      GameInUse(IdentityCardData("33000010", "Marc Arndt"), EnvelopeData("44000013"))))
  }

  /**
   * Checks that an identity card that currently has no borrowed games can't borrow an already borrowed game in unlimited mode.
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def alreadyLendUnlimitedIssueGames2(): Unit = {
    issueGamesService.issueGames(IssueGameRequest("33000101", Array("11000014"), false)) should equal(LendingEntityInUse(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"),
      GameInUse(IdentityCardData("33000010", "Marc Arndt"), EnvelopeData("44000013"))))
  }

  /**
   * Checks that an identity card that currently has no borrowed games can't borrow an already borrowed game in limited mode.
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def alreadyLendLimitedIssueGames2(): Unit = {
    issueGamesService.issueGames(IssueGameRequest("33000101", Array("11000014"), true)) should equal(LendingEntityInUse(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"),
      GameInUse(IdentityCardData("33000010", "Marc Arndt"), EnvelopeData("44000013"))))
  }

  /**
   * Checks that an currently not issued identity card can't borrow a game in unlimited mode.
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def notBorrowedIdentityCardUnlimitedIssueGames(): Unit = {
    issueGamesService.issueGames(IssueGameRequest("33000032", Array("11000058"), false)) should equal(LendingEntityInUse(IdentityCardData("33000032"), NotInUse()))
  }

  /**
   * Checks that an currently not issued identity card can't borrow a game in limited mode.
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def notBorrowedIdentityCardLimitedIssueGames(): Unit = {
    issueGamesService.issueGames(IssueGameRequest("33000032", Array("11000058"), true)) should equal(LendingEntityInUse(IdentityCardData("33000032"), NotInUse()))
  }

  /**
   * Checks that a not activated identity card can't borrow a game.
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def notActivatedIdentityCardIssueGames(): Unit = {
    issueGamesService.issueGames(IssueGameRequest("33000043", Array("11000058"), true)) should equal(LendingEntityNotExists("33000043"))
  }

  /**
   * Checks that a not existing identity card can't borrow a game.
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def notExistingIdentityCardIssueGames(): Unit = {
    issueGamesService.issueGames(IssueGameRequest("33000054", Array("11000058"), true)) should equal(LendingEntityNotExists("33000054"))
  }

  /**
   * Checks that a not activated game can't be borrowed
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def notActivatedGameIssueGames(): Unit = {
    issueGamesService.issueGames(IssueGameRequest("33000101", Array("11000058", "11000070"), true)) should equal(LendingEntityNotExists("11000070"))
  }

  /**
   * Checks that a not existing game can't be borrowed
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def notExistingGameIssueGames(): Unit = {
    issueGamesService.issueGames(IssueGameRequest("33000101", Array("11000058", "11000081"), true)) should equal(LendingEntityNotExists("11000081"))
  }

  /**
   * Checks that an incorrect barcode is detected and not processed
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def incorrectBarcodeIssueGames(): Unit = {
    issueGamesService.issueGames(IssueGameRequest("33000011", Array("11000058", "11000081"), true)) should equal(IncorrectBarcode("33000011"))
    issueGamesService.issueGames(IssueGameRequest("33000101", Array("11000013"), true)) should equal(IncorrectBarcode("11000013"))
  }
}