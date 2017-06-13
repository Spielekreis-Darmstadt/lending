package info.armado.ausleihe.remote

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitSuite
import org.scalatest.Matchers.convertToAnyShouldWrapper
import org.scalatest.Matchers.equal
import org.jboss.arquillian.junit.Arquillian
import javax.inject.Inject
import info.armado.ausleihe.remote.requests.IssueIdentityCardRequest
import info.armado.ausleihe.remote.results.IssueIdentityCardSuccess
import info.armado.ausleihe.remote.dataobjects.entities.IdentityCardData
import org.jboss.arquillian.persistence.UsingDataSet
import info.armado.ausleihe.remote.dataobjects.entities.EnvelopeData
import org.junit.Test
import info.armado.ausleihe.remote.results.LendingEntityInUse
import info.armado.ausleihe.remote.dataobjects.inuse.IdentityCardInUse
import info.armado.ausleihe.remote.dataobjects.entities.GameData
import info.armado.ausleihe.remote.dataobjects.inuse.EnvelopeInUse
import info.armado.ausleihe.remote.results.LendingEntityNotExists
import info.armado.ausleihe.remote.results.IncorrectBarcode

object IssueIdentityCardsTest extends WebDeployment

@RunWith(classOf[Arquillian])
class IssueIdentityCardsTest extends JUnitSuite {
  @Inject var issueIdentityCardsService: IssueIdentityCards = _

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def successfulIssueIdentityCard(): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000032", "44000035")) should equal(
      IssueIdentityCardSuccess(IdentityCardData("33000032"), EnvelopeData("44000035")))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def alreadyIssuedIdentityCardWithGamesIssueIdentityCard(): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000010", "44000035")) should equal(
      LendingEntityInUse(IdentityCardData("33000010", "Marc Arndt"), IdentityCardInUse(EnvelopeData("44000013"),
        Array(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameData("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"), GameData("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120")))))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def alreadyIssuedIdentityCardWithoutGamesIssueIdentityCard(): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000101", "44000035")) should equal(
      LendingEntityInUse(IdentityCardData("33000101"), IdentityCardInUse(EnvelopeData("44000104"), Array())))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def alreadyIssuedEnvelopeIssueIdentityCard(): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000032", "44000013")) should equal(
      LendingEntityInUse(EnvelopeData("44000013"), EnvelopeInUse(IdentityCardData("33000010", "Marc Arndt"),
        Array(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameData("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"), GameData("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120")))))

  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def notActivatedIdentityCardIssueIdentityCard(): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000043", "44000035")) should equal(LendingEntityNotExists("33000043"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def notActivatedEnvelopeIssueIdentityCard(): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000032", "44000046")) should equal(LendingEntityNotExists("44000046"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def notExistingIdentityCardIssueIdentityCard(): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000054", "44000035")) should equal(LendingEntityNotExists("33000054"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def notExistingEnvelopeIssueIdentityCard(): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000032", "44000057")) should equal(LendingEntityNotExists("44000057"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def incorrectIdentityCardBarcodeIssueIdentityCard(): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000011", "44000035")) should equal(IncorrectBarcode("33000011"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def incorrectEnvelopeBarcodeIssueIdentityCard(): Unit = {
    issueIdentityCardsService.issueIdentityCard(IssueIdentityCardRequest("33000032", "44000014")) should equal(IncorrectBarcode("44000014"))
  }
}