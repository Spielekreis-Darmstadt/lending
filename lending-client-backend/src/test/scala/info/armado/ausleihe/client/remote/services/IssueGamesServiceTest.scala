package info.armado.ausleihe.client.remote.services

import info.armado.ausleihe.client.transport.dataobjects.entities.{
  EnvelopeDTO,
  GameDTO,
  IdentityCardDTO
}
import info.armado.ausleihe.client.transport.dataobjects.inuse.{GameInUseDTO, NotInUseDTO}
import info.armado.ausleihe.client.transport.requests.IssueGameRequestDTO
import info.armado.ausleihe.client.transport.results.{IncorrectBarcodeDTO, IssueGamesSuccessDTO, _}
import org.arquillian.ape.rdbms.{ShouldMatchDataSet, UsingDataSet}
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource
import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.{convertToAnyShouldWrapper, equal}
import org.scalatest.junit.JUnitSuite

object IssueGamesServiceTest extends WebDeployment

@RunWith(classOf[Arquillian])
class IssueGamesServiceTest extends JUnitSuite {

  /**
    * Checks that a currently not borrowed game can be lend to an identity card that has currently borrowed at least one game in unlimited mode.
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(
    value = Array("datasets/lend-game1.xml"),
    excludeColumns = Array("LENDGAME.ID", "LENDGAME.LENDTIME")
  )
  def successfulUnlimitedIssueGames(
      @ArquillianResteasyResource issueGamesService: IssueGamesService
  ): Unit = {
    issueGamesService.issueGames(
      IssueGameRequestDTO("33000010", Array("11000058"), false)
    ) should equal(
      IssueGamesSuccessDTO(
        IdentityCardDTO("33000010", "Marc Arndt"),
        Array(
          GameDTO("11000058", "Titel 4", "Autor 3", "Verlag 2", "13", "3 - 5", "90 - 120", 2016)
        )
      )
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(
    value = Array("datasets/lend-game2.xml"),
    excludeColumns = Array("LENDGAME.ID", "LENDGAME.LENDTIME")
  )
  def successfulLimitedIssueGames(
      @ArquillianResteasyResource issueGamesService: IssueGamesService
  ): Unit = {
    issueGamesService.issueGames(
      IssueGameRequestDTO("33000101", Array("11000058"), true)
    ) should equal(
      IssueGamesSuccessDTO(
        IdentityCardDTO("33000101"),
        Array(
          GameDTO("11000058", "Titel 4", "Autor 3", "Verlag 2", "13", "3 - 5", "90 - 120", 2016)
        )
      )
    )
  }

  /**
    * Checks that an identity card that has currently borrowed at least one game can't borrow another game in limited mode.
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def alreadyIssuedIdentityCardIssueGames(
      @ArquillianResteasyResource issueGamesService: IssueGamesService
  ): Unit = {
    issueGamesService.issueGames(
      IssueGameRequestDTO("33000010", Array("11000058"), true)
    ) should equal(
      IdentityCardHasIssuedGamesDTO(
        IdentityCardDTO("33000010", "Marc Arndt"),
        Array(
          GameDTO("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120", 2016),
          GameDTO("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120", null),
          GameDTO("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120", 2015)
        )
      )
    )
  }

  /**
    * Checks that an identity card that currently has at least one borrowed game can't borrow an already borrowed game in unlimited mode.
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def alreadyLendUnlimitedIssueGames1(
      @ArquillianResteasyResource issueGamesService: IssueGamesService
  ): Unit = {
    issueGamesService.issueGames(
      IssueGameRequestDTO("33000010", Array("11000014"), false)
    ) should equal(
      LendingEntityInUseDTO(
        GameDTO("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120", 2016),
        GameInUseDTO(
          IdentityCardDTO("33000010", "Marc Arndt"),
          EnvelopeDTO("44000013")
        )
      )
    )
  }

  /**
    * Checks that an identity card that currently has at least one borrowed game can't borrow an already borrowed game in limited mode.
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def alreadyLendLimitedIssueGames1(
      @ArquillianResteasyResource issueGamesService: IssueGamesService
  ): Unit = {
    issueGamesService.issueGames(
      IssueGameRequestDTO("33000010", Array("11000014"), true)
    ) should equal(
      LendingEntityInUseDTO(
        GameDTO("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120", 2016),
        GameInUseDTO(
          IdentityCardDTO("33000010", "Marc Arndt"),
          EnvelopeDTO("44000013")
        )
      )
    )
  }

  /**
    * Checks that an identity card that currently has no borrowed games can't borrow an already borrowed game in unlimited mode.
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def alreadyLendUnlimitedIssueGames2(
      @ArquillianResteasyResource issueGamesService: IssueGamesService
  ): Unit = {
    issueGamesService.issueGames(
      IssueGameRequestDTO("33000101", Array("11000014"), false)
    ) should equal(
      LendingEntityInUseDTO(
        GameDTO("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120", 2016),
        GameInUseDTO(
          IdentityCardDTO("33000010", "Marc Arndt"),
          EnvelopeDTO("44000013")
        )
      )
    )
  }

  /**
    * Checks that an identity card that currently has no borrowed games can't borrow an already borrowed game in limited mode.
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def alreadyLendLimitedIssueGames2(
      @ArquillianResteasyResource issueGamesService: IssueGamesService
  ): Unit = {
    issueGamesService.issueGames(
      IssueGameRequestDTO("33000101", Array("11000014"), true)
    ) should equal(
      LendingEntityInUseDTO(
        GameDTO("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120", 2016),
        GameInUseDTO(
          IdentityCardDTO("33000010", "Marc Arndt"),
          EnvelopeDTO("44000013")
        )
      )
    )
  }

  /**
    * Checks that an currently not issued identity card can't borrow a game in unlimited mode.
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notBorrowedIdentityCardUnlimitedIssueGames(
      @ArquillianResteasyResource issueGamesService: IssueGamesService
  ): Unit = {
    issueGamesService.issueGames(
      IssueGameRequestDTO("33000032", Array("11000058"), false)
    ) should equal(
      LendingEntityInUseDTO(IdentityCardDTO("33000032"), NotInUseDTO())
    )
  }

  /**
    * Checks that an currently not issued identity card can't borrow a game in limited mode.
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notBorrowedIdentityCardLimitedIssueGames(
      @ArquillianResteasyResource issueGamesService: IssueGamesService
  ): Unit = {
    issueGamesService.issueGames(
      IssueGameRequestDTO("33000032", Array("11000058"), true)
    ) should equal(
      LendingEntityInUseDTO(IdentityCardDTO("33000032"), NotInUseDTO())
    )
  }

  /**
    * Checks that a not activated identity card can't borrow a game.
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notActivatedIdentityCardIssueGames(
      @ArquillianResteasyResource issueGamesService: IssueGamesService
  ): Unit = {
    issueGamesService.issueGames(
      IssueGameRequestDTO("33000043", Array("11000058"), true)
    ) should equal(LendingEntityNotExistsDTO("33000043"))
  }

  /**
    * Checks that a not existing identity card can't borrow a game.
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notExistingIdentityCardIssueGames(
      @ArquillianResteasyResource issueGamesService: IssueGamesService
  ): Unit = {
    issueGamesService.issueGames(
      IssueGameRequestDTO("33000054", Array("11000058"), true)
    ) should equal(LendingEntityNotExistsDTO("33000054"))
  }

  /**
    * Checks that a not activated game can't be borrowed
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notActivatedGameIssueGames(
      @ArquillianResteasyResource issueGamesService: IssueGamesService
  ): Unit = {
    issueGamesService.issueGames(
      IssueGameRequestDTO("33000101", Array("11000058", "11000070"), true)
    ) should equal(LendingEntityNotExistsDTO("11000070"))
  }

  /**
    * Checks that a not existing game can't be borrowed
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notExistingGameIssueGames(
      @ArquillianResteasyResource issueGamesService: IssueGamesService
  ): Unit = {
    issueGamesService.issueGames(
      IssueGameRequestDTO("33000101", Array("11000058", "11000081"), true)
    ) should equal(LendingEntityNotExistsDTO("11000081"))
  }

  /**
    * Checks that an incorrect barcode is detected and not processed
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def incorrectBarcodeIssueGames(
      @ArquillianResteasyResource issueGamesService: IssueGamesService
  ): Unit = {
    issueGamesService.issueGames(
      IssueGameRequestDTO("33000011", Array("11000058", "11000081"), true)
    ) should equal(IncorrectBarcodeDTO("33000011"))
    issueGamesService.issueGames(
      IssueGameRequestDTO("33000101", Array("11000013"), true)
    ) should equal(IncorrectBarcodeDTO("11000013"))
  }
}
