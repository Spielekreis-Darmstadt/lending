package info.armado.ausleihe.remote

import javax.inject.Inject

import info.armado.ausleihe.remote.dataobjects.entities.{EnvelopeData, GameData, IdentityCardData}
import info.armado.ausleihe.remote.dataobjects.inuse.NotInUse
import info.armado.ausleihe.remote.requests.ReturnIdentityCardRequest
import info.armado.ausleihe.remote.results._
import org.arquillian.ape.rdbms.UsingDataSet
import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.{convertToAnyShouldWrapper, equal}
import org.scalatest.junit.JUnitSuite

object ReturnIdentityCardsTest extends WebDeployment

@RunWith(classOf[Arquillian])
class ReturnIdentityCardsTest extends JUnitSuite {
  @Inject var returnIdentityCardsService: ReturnIdentityCards = _

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def successfulReturnIdentityCard(): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000101", "44000104")) should equal(
      ReturnIdentityCardSuccess(IdentityCardData("33000101"), EnvelopeData("44000104")))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def identityCardHasBorrowedGamesReturnIdentityCard(): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000010", "44000013")) should equal(
      IdentityCardHasIssuedGames(IdentityCardData("33000010", "Marc Arndt"),
        Array(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameData("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"), GameData("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"))))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def identityCardDoesNotBelongToEnvelopeReturnIdentityCard(): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000101", "44000013")) should equal(
      IdentityCardEnvelopeNotBound(IdentityCardData("33000101"), EnvelopeData("44000013"), EnvelopeData("44000104")))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def identityCardNotIssuedReturnIdentityCard(): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000032", "44000013")) should equal(
      LendingEntityInUse(IdentityCardData("33000032"), NotInUse()))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def envelopeNotIssuedReturnIdentityCard(): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000010", "44000035")) should equal(
      LendingEntityInUse(EnvelopeData("44000035"), NotInUse()))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def identityCardNotActivatedReturnIdentityCard(): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000043", "44000013")) should equal(
      LendingEntityNotExists("33000043"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def envelopeNotActivatedReturnIdentityCard(): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000010", "44000046")) should equal(
      LendingEntityNotExists("44000046"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def identityCardNotExistsReturnIdentityCard(): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000054", "44000013")) should equal(
      LendingEntityNotExists("33000054"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def envelopeNotExistsReturnIdentityCard(): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000010", "44000057")) should equal(
      LendingEntityNotExists("44000057"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def incorrectIdentityCardBarcodeReturnIdentityCard(): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000011", "44000104")) should equal(
      IncorrectBarcode("33000011"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def incorrectEnvelopeBarcodeReturnIdentityCard(): Unit = {
    returnIdentityCardsService.returnIdentityCard(ReturnIdentityCardRequest("33000101", "44000014")) should equal(
      IncorrectBarcode("44000014"))
  }
}