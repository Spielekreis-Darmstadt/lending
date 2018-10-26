package info.armado.ausleihe.remote

import info.armado.ausleihe.remote.dataobjects.entities.{EnvelopeData, GameData, IdentityCardData}
import info.armado.ausleihe.remote.dataobjects.inuse.{EnvelopeInUse, IdentityCardInUse}
import info.armado.ausleihe.remote.requests.IssueIdentityCardRequest
import info.armado.ausleihe.remote.results.{IncorrectBarcode, IssueIdentityCardSuccess, LendingEntityInUse, LendingEntityNotExists}
import org.arquillian.ape.rdbms.{ShouldMatchDataSet, UsingDataSet}
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource
import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.{convertToAnyShouldWrapper, equal}
import org.scalatest.junit.JUnitSuite

object IssueIdentityCardsTest extends WebDeployment

@RunWith(classOf[Arquillian])
class IssueIdentityCardsTest extends JUnitSuite {
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(value = Array("datasets/issue-identity-card.xml"), excludeColumns = Array("LENDIDENTITYCARD.ID", "LENDIDENTITYCARD.LENDTIME"))
  def successfulIssueIdentityCard(@ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCards): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000032", "44000035")) should equal(
      IssueIdentityCardSuccess(IdentityCardData("33000032"), EnvelopeData("44000035")))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def alreadyIssuedIdentityCardWithGamesIssueIdentityCard(@ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCards): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000010", "44000035")) should equal(
      LendingEntityInUse(IdentityCardData("33000010", "Marc Arndt"), IdentityCardInUse(EnvelopeData("44000013"),
        Array(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameData("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"), GameData("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120")))))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def alreadyIssuedIdentityCardWithoutGamesIssueIdentityCard(@ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCards): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000101", "44000035")) should equal(
      LendingEntityInUse(IdentityCardData("33000101"), IdentityCardInUse(EnvelopeData("44000104"), Array())))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def alreadyIssuedEnvelopeIssueIdentityCard(@ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCards): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000032", "44000013")) should equal(
      LendingEntityInUse(EnvelopeData("44000013"), EnvelopeInUse(IdentityCardData("33000010", "Marc Arndt"),
        Array(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameData("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"), GameData("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120")))))

  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notActivatedIdentityCardIssueIdentityCard(@ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCards): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000043", "44000035")) should equal(LendingEntityNotExists("33000043"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notActivatedEnvelopeIssueIdentityCard(@ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCards): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000032", "44000046")) should equal(LendingEntityNotExists("44000046"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notExistingIdentityCardIssueIdentityCard(@ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCards): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000054", "44000035")) should equal(LendingEntityNotExists("33000054"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notExistingEnvelopeIssueIdentityCard(@ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCards): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000032", "44000057")) should equal(LendingEntityNotExists("44000057"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def incorrectIdentityCardBarcodeIssueIdentityCard(@ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCards): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000011", "44000035")) should equal(IncorrectBarcode("33000011"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def incorrectEnvelopeBarcodeIssueIdentityCard(@ArquillianResteasyResource issueIdentityCardsService: IssueIdentityCards): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000032", "44000014")) should equal(IncorrectBarcode("44000014"))
  }
}