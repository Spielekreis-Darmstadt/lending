package info.armado.ausleihe.remote

import javax.inject.Inject

import info.armado.ausleihe.remote.dataobjects.entities.{EnvelopeData, GameData, IdentityCardData}
import info.armado.ausleihe.remote.dataobjects.inuse.{EnvelopeInUse, GameInUse, IdentityCardInUse, NotInUse}
import info.armado.ausleihe.remote.requests.GameInformationRequest
import info.armado.ausleihe.remote.results.{IncorrectBarcode, Information, LendingEntityInUse, LendingEntityNotExists}
import org.arquillian.ape.rdbms.{ShouldMatchDataSet, UsingDataSet}
import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.{be, convertToAnyShouldWrapper, equal}
import org.scalatest.junit.JUnitSuite

object UniversalTest extends WebDeployment

@RunWith(classOf[Arquillian])
class UniversalTest extends JUnitSuite {
  @Inject var universalService: Universal = _

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  @ShouldMatchDataSet(Array("datasets/full.xml"))
  def gamesBarcodeInUse(): Unit = {
    // test if an activated game can be correctly found
    universalService.barcodeInUse("11000014") should equal(LendingEntityInUse(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameInUse(IdentityCardData("33000010", "Marc Arndt"), EnvelopeData("44000013"))))
    universalService.barcodeInUse("11000058") should equal(LendingEntityInUse(GameData("11000058", "Titel 4", "Autor 3", "Verlag 2", "13", "3 - 5", "90 - 120"), NotInUse()))
    // test if an not activated game can't be found
    universalService.barcodeInUse("11000070") should equal(LendingEntityNotExists("11000070"))
    // test if a not existing game can't be found
    universalService.barcodeInUse("11000081") should equal(LendingEntityNotExists("11000081"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  @ShouldMatchDataSet(Array("datasets/full.xml"))
  def identityCardBarcodeInUse(): Unit = {
    universalService.barcodeInUse("33000010") should equal(LendingEntityInUse(IdentityCardData("33000010", "Marc Arndt"), IdentityCardInUse(EnvelopeData("44000013"),
      Array(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameData("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"), GameData("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120")))))
    universalService.barcodeInUse("33000032") should equal(LendingEntityInUse(IdentityCardData("33000032"), NotInUse()))
    universalService.barcodeInUse("33000043") should equal(LendingEntityNotExists("33000043"))
    universalService.barcodeInUse("33000054") should equal(LendingEntityNotExists("33000054"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  @ShouldMatchDataSet(Array("datasets/full.xml"))
  def envelopeBarcodeInUse(): Unit = {
    universalService.barcodeInUse("44000013") should equal(LendingEntityInUse(EnvelopeData("44000013"), EnvelopeInUse(IdentityCardData("33000010", "Marc Arndt"),
      Array(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameData("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"), GameData("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120")))))
    universalService.barcodeInUse("44000035") should equal(LendingEntityInUse(EnvelopeData("44000035"), NotInUse()))
    universalService.barcodeInUse("44000046") should equal(LendingEntityNotExists("44000046"))
    universalService.barcodeInUse("44000057") should equal(LendingEntityNotExists("44000057"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  @ShouldMatchDataSet(Array("datasets/full.xml"))
  def incorrectBarcodeInUse(): Unit = {
    universalService.barcodeInUse("11000013") should equal(IncorrectBarcode("11000013"))
    universalService.barcodeInUse("33000011") should equal(IncorrectBarcode("33000011"))
    universalService.barcodeInUse("44000012") should equal(IncorrectBarcode("44000012"))
    universalService.barcodeInUse("123") should equal(IncorrectBarcode("123"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  @ShouldMatchDataSet(Array("datasets/full.xml"))
  def gamesStatusInformation(): Unit = {
    universalService.statusInformation("11000014") should equal(Information(Array(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120")), IdentityCardData("33000010", "Marc Arndt"), EnvelopeData("44000013")))
    universalService.statusInformation("11000025") should equal(Information(Array(GameData("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120")), IdentityCardData("33000010", "Marc Arndt"), EnvelopeData("44000013")))
    universalService.statusInformation("11000047") should equal(Information(Array(GameData("11000047", "Titel 3", "Autor 2", "Verlag 3", "12", "2 - 4", "90 - 120")), IdentityCardData("33000021"), EnvelopeData("44000024")))
    universalService.statusInformation("11000058") should equal(Information(Array(GameData("11000058", "Titel 4", "Autor 3", "Verlag 2", "13", "3 - 5", "90 - 120")), null, null))
    universalService.statusInformation("11000069") should equal(Information(Array(GameData("11000069", "Titel 4", "Autor 3", "Verlag 2", "13", "3 - 5", "90 - 120")), null, null))
    // test if an not activated game can't be found
    universalService.statusInformation("11000070") should equal(LendingEntityNotExists("11000070"))
    // test if a not existing game can't be found
    universalService.statusInformation("11000081") should equal(LendingEntityNotExists("11000081"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  @ShouldMatchDataSet(Array("datasets/full.xml"))
  def identityCardStatusInformation(): Unit = {
    universalService.statusInformation("33000010") should equal(Information(
        Array(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameData("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"), GameData("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120")), 
        IdentityCardData("33000010", "Marc Arndt"), EnvelopeData("44000013")))
    universalService.statusInformation("33000021") should equal(Information(Array(GameData("11000047", "Titel 3", "Autor 2", "Verlag 3", "12", "2 - 4", "90 - 120")), IdentityCardData("33000021"), EnvelopeData("44000024")))
    universalService.statusInformation("33000032") should equal(Information(Array(), IdentityCardData("33000032"), null))
    universalService.statusInformation("33000043") should equal(LendingEntityNotExists("33000043"))
    universalService.statusInformation("33000054") should equal(LendingEntityNotExists("33000054"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  @ShouldMatchDataSet(Array("datasets/full.xml"))
  def envelopeStatusInformation(): Unit = {
    universalService.statusInformation("44000013") should equal(Information(
        Array(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameData("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"), GameData("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120")), 
        IdentityCardData("33000010", "Marc Arndt"), EnvelopeData("44000013")))
    universalService.statusInformation("44000024") should equal(Information(Array(GameData("11000047", "Titel 3", "Autor 2", "Verlag 3", "12", "2 - 4", "90 - 120")), IdentityCardData("33000021"), EnvelopeData("44000024")))
    universalService.statusInformation("44000035") should equal(Information(Array(), null, EnvelopeData("44000035")))
    universalService.statusInformation("44000046") should equal(LendingEntityNotExists("44000046"))
    universalService.statusInformation("44000057") should equal(LendingEntityNotExists("44000057"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  @ShouldMatchDataSet(Array("datasets/full.xml"))
  def incorrectBarcodeStatusInformation(): Unit = {
    universalService.statusInformation("11000013") should equal(IncorrectBarcode("11000013"))
    universalService.statusInformation("33000011") should equal(IncorrectBarcode("33000011"))
    universalService.statusInformation("44000012") should equal(IncorrectBarcode("44000012"))
    universalService.statusInformation("123") should equal(IncorrectBarcode("123"))
  }

  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  @ShouldMatchDataSet(Array("datasets/full.xml"))
  def gamesInformation(): Unit = {
    universalService.gamesInformation(GameInformationRequest("Titel", null, null, null, null, null, null)).foundGames.length should be(6)
    universalService.gamesInformation(GameInformationRequest("Titel 1", null, null, null, null, null, null)).foundGames.length should be(1)
    universalService.gamesInformation(GameInformationRequest("Titel 2", null, null, null, null, null, null)).foundGames.length should be(2)
    universalService.gamesInformation(GameInformationRequest("Autor 1", null, null, null, null, null, null)).foundGames.length should be(3)
    universalService.gamesInformation(GameInformationRequest("Verlag 2", null, null, null, null, null, null)).foundGames.length should be(4)
  }
}