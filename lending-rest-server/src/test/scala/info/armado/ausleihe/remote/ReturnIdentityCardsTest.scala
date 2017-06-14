package info.armado.ausleihe.remote

import info.armado.ausleihe.remote.dataobjects.entities.{EnvelopeData, GameData, IdentityCardData}
import info.armado.ausleihe.remote.dataobjects.inuse.NotInUse
import info.armado.ausleihe.remote.requests.ReturnIdentityCardRequest
import info.armado.ausleihe.remote.results._
import org.arquillian.ape.rdbms.{ShouldMatchDataSet, UsingDataSet}
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource
import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.{convertToAnyShouldWrapper, equal}
import org.scalatest.junit.JUnitSuite

object ReturnIdentityCardsTest extends WebDeployment

@RunWith(classOf[Arquillian])
class ReturnIdentityCardsTest extends JUnitSuite {
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(value = Array("datasets/return-identity-card.xml"), excludeColumns = Array("LENDIDENTITYCARD.RETURNTIME"))
  def successfulReturnIdentityCard(@ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCards): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000101", "44000104")) should equal(
      ReturnIdentityCardSuccess(IdentityCardData("33000101"), EnvelopeData("44000104")))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def identityCardHasBorrowedGamesReturnIdentityCard(@ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCards): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000010", "44000013")) should equal(
      IdentityCardHasIssuedGames(IdentityCardData("33000010", "Marc Arndt"),
        Array(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameData("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"), GameData("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"))))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def identityCardDoesNotBelongToEnvelopeReturnIdentityCard(@ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCards): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000101", "44000013")) should equal(
      IdentityCardEnvelopeNotBound(IdentityCardData("33000101"), EnvelopeData("44000013"), EnvelopeData("44000104")))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def identityCardNotIssuedReturnIdentityCard(@ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCards): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000032", "44000013")) should equal(
      LendingEntityInUse(IdentityCardData("33000032"), NotInUse()))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def envelopeNotIssuedReturnIdentityCard(@ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCards): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000010", "44000035")) should equal(
      LendingEntityInUse(EnvelopeData("44000035"), NotInUse()))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def identityCardNotActivatedReturnIdentityCard(@ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCards): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000043", "44000013")) should equal(
      LendingEntityNotExists("33000043"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def envelopeNotActivatedReturnIdentityCard(@ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCards): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000010", "44000046")) should equal(
      LendingEntityNotExists("44000046"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def identityCardNotExistsReturnIdentityCard(@ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCards): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000054", "44000013")) should equal(
      LendingEntityNotExists("33000054"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def envelopeNotExistsReturnIdentityCard(@ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCards): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000010", "44000057")) should equal(
      LendingEntityNotExists("44000057"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def incorrectIdentityCardBarcodeReturnIdentityCard(@ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCards): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000011", "44000104")) should equal(
      IncorrectBarcode("33000011"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def incorrectEnvelopeBarcodeReturnIdentityCard(@ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCards): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000101", "44000014")) should equal(
      IncorrectBarcode("44000014"))
  }
}