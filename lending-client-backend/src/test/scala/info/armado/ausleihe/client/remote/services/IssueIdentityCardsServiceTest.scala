package info.armado.ausleihe.client.remote.services

import info.armado.ausleihe.client.transport.dataobjects.entities._
import info.armado.ausleihe.client.transport.dataobjects.inuse._
import info.armado.ausleihe.client.transport.requests.IssueIdentityCardRequestDTO
import info.armado.ausleihe.client.transport.results._
import org.arquillian.ape.rdbms.{ShouldMatchDataSet, UsingDataSet}
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource
import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.{convertToAnyShouldWrapper, equal}
import org.scalatest.junit.JUnitSuite

object IssueIdentityCardsServiceTest extends WebDeployment

@RunWith(classOf[Arquillian])
class IssueIdentityCardsServiceTest extends JUnitSuite {
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(
    value = Array("datasets/issue-identity-card.xml"),
    excludeColumns = Array("LENDIDENTITYCARD.ID", "LENDIDENTITYCARD.LENDTIME")
  )
  def successfulIssueIdentityCard(
      @ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCardsService
  ): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequestDTO("33000032", "44000035")) should equal(
      IssueIdentityCardSuccessDTO(IdentityCardDTO("33000032"), EnvelopeDTO("44000035"))
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def alreadyIssuedIdentityCardWithGamesIssueIdentityCard(
      @ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCardsService
  ): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequestDTO("33000010", "44000035")) should equal(
      LendingEntityInUseDTO(
        IdentityCardDTO("33000010", "Marc Arndt"),
        IdentityCardInUseDTO(
          EnvelopeDTO("44000013"),
          Array(
            GameDTO("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120", 2016),
            GameDTO("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120", null),
            GameDTO("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120", 2015)
          )
        )
      )
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def alreadyIssuedIdentityCardWithoutGamesIssueIdentityCard(
      @ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCardsService
  ): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequestDTO("33000101", "44000035")) should equal(
      LendingEntityInUseDTO(
        IdentityCardDTO("33000101"),
        IdentityCardInUseDTO(EnvelopeDTO("44000104"), Array())
      )
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def alreadyIssuedEnvelopeIssueIdentityCard(
      @ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCardsService
  ): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequestDTO("33000032", "44000013")) should equal(
      LendingEntityInUseDTO(
        EnvelopeDTO("44000013"),
        EnvelopeInUseDTO(
          IdentityCardDTO("33000010", "Marc Arndt"),
          Array(
            GameDTO("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120", 2016),
            GameDTO("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120", null),
            GameDTO("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120", 2015)
          )
        )
      )
    )

  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notActivatedIdentityCardIssueIdentityCard(
      @ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCardsService
  ): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequestDTO("33000043", "44000035")) should equal(
      LendingEntityNotExistsDTO("33000043")
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notActivatedEnvelopeIssueIdentityCard(
      @ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCardsService
  ): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequestDTO("33000032", "44000046")) should equal(
      LendingEntityNotExistsDTO("44000046")
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notExistingIdentityCardIssueIdentityCard(
      @ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCardsService
  ): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequestDTO("33000054", "44000035")) should equal(
      LendingEntityNotExistsDTO("33000054")
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notExistingEnvelopeIssueIdentityCard(
      @ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCardsService
  ): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequestDTO("33000032", "44000057")) should equal(
      LendingEntityNotExistsDTO("44000057")
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def incorrectIdentityCardBarcodeIssueIdentityCard(
      @ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCardsService
  ): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequestDTO("33000011", "44000035")) should equal(
      IncorrectBarcodeDTO("33000011")
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def incorrectEnvelopeBarcodeIssueIdentityCard(
      @ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCardsService
  ): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequestDTO("33000032", "44000014")) should equal(
      IncorrectBarcodeDTO("44000014")
    )
  }
}
