package info.armado.ausleihe.client.remote.services

import info.armado.ausleihe.client.transport.dataobjects.entities.{EnvelopeDTO, GameDTO, IdentityCardDTO}
import info.armado.ausleihe.client.transport.dataobjects.inuse.{EnvelopeInUseDTO, GameInUseDTO, IdentityCardInUseDTO, NotInUseDTO}
import info.armado.ausleihe.client.transport.requests.GameInformationRequestDTO
import info.armado.ausleihe.client.transport.results.{IncorrectBarcodeDTO, InformationDTO, LendingEntityInUseDTO, LendingEntityNotExistsDTO}
import org.arquillian.ape.rdbms.{ShouldMatchDataSet, UsingDataSet}
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource
import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.{be, convertToAnyShouldWrapper, equal}
import org.scalatest.junit.JUnitSuite

object UniversalServiceTest extends WebDeployment

@RunWith(classOf[Arquillian])
class UniversalServiceTest extends JUnitSuite {
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def gamesBarcodeInUse(@ArquillianResteasyResource universalService: UniversalService): Unit = {
    // test if an activated game can be correctly found
    universalService.barcodeInUse("11000014") should equal(LendingEntityInUseDTO(GameDTO("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameInUseDTO(IdentityCardDTO("33000010", "Marc Arndt"), EnvelopeDTO("44000013"))))
    universalService.barcodeInUse("11000058") should equal(LendingEntityInUseDTO(GameDTO("11000058", "Titel 4", "Autor 3", "Verlag 2", "13", "3 - 5", "90 - 120"), NotInUseDTO()))
    // test if an not activated game can't be found
    universalService.barcodeInUse("11000070") should equal(LendingEntityNotExistsDTO("11000070"))
    // test if a not existing game can't be found
    universalService.barcodeInUse("11000081") should equal(LendingEntityNotExistsDTO("11000081"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def identityCardBarcodeInUse(@ArquillianResteasyResource universalService: UniversalService): Unit = {
    universalService.barcodeInUse("33000010") should equal(LendingEntityInUseDTO(IdentityCardDTO("33000010", "Marc Arndt"), IdentityCardInUseDTO(EnvelopeDTO("44000013"),
      Array(GameDTO("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameDTO("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"), GameDTO("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120")))))
    universalService.barcodeInUse("33000032") should equal(LendingEntityInUseDTO(IdentityCardDTO("33000032"), NotInUseDTO()))
    universalService.barcodeInUse("33000043") should equal(LendingEntityNotExistsDTO("33000043"))
    universalService.barcodeInUse("33000054") should equal(LendingEntityNotExistsDTO("33000054"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def envelopeBarcodeInUse(@ArquillianResteasyResource universalService: UniversalService): Unit = {
    universalService.barcodeInUse("44000013") should equal(LendingEntityInUseDTO(EnvelopeDTO("44000013"), EnvelopeInUseDTO(IdentityCardDTO("33000010", "Marc Arndt"),
      Array(GameDTO("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameDTO("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"), GameDTO("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120")))))
    universalService.barcodeInUse("44000035") should equal(LendingEntityInUseDTO(EnvelopeDTO("44000035"), NotInUseDTO()))
    universalService.barcodeInUse("44000046") should equal(LendingEntityNotExistsDTO("44000046"))
    universalService.barcodeInUse("44000057") should equal(LendingEntityNotExistsDTO("44000057"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def incorrectBarcodeInUse(@ArquillianResteasyResource universalService: UniversalService): Unit = {
    universalService.barcodeInUse("11000013") should equal(IncorrectBarcodeDTO("11000013"))
    universalService.barcodeInUse("33000011") should equal(IncorrectBarcodeDTO("33000011"))
    universalService.barcodeInUse("44000012") should equal(IncorrectBarcodeDTO("44000012"))
    universalService.barcodeInUse("123") should equal(IncorrectBarcodeDTO("123"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def gamesStatusInformation(@ArquillianResteasyResource universalService: UniversalService): Unit = {
    universalService.statusInformation("11000014") should equal(InformationDTO(Array(GameDTO("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120")), IdentityCardDTO("33000010", "Marc Arndt"), EnvelopeDTO("44000013")))
    universalService.statusInformation("11000025") should equal(InformationDTO(Array(GameDTO("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120")), IdentityCardDTO("33000010", "Marc Arndt"), EnvelopeDTO("44000013")))
    universalService.statusInformation("11000047") should equal(InformationDTO(Array(GameDTO("11000047", "Titel 3", "Autor 2", "Verlag 3", "12", "2 - 4", "90 - 120")), IdentityCardDTO("33000021"), EnvelopeDTO("44000024")))
    universalService.statusInformation("11000058") should equal(InformationDTO(Array(GameDTO("11000058", "Titel 4", "Autor 3", "Verlag 2", "13", "3 - 5", "90 - 120")), null, null))
    universalService.statusInformation("11000069") should equal(InformationDTO(Array(GameDTO("11000069", "Titel 4", "Autor 3", "Verlag 2", "13", "3 - 5", "90 - 120")), null, null))
    // test if an not activated game can't be found
    universalService.statusInformation("11000070") should equal(LendingEntityNotExistsDTO("11000070"))
    // test if a not existing game can't be found
    universalService.statusInformation("11000081") should equal(LendingEntityNotExistsDTO("11000081"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def identityCardStatusInformation(@ArquillianResteasyResource universalService: UniversalService): Unit = {
    universalService.statusInformation("33000010") should equal(InformationDTO(
      Array(GameDTO("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameDTO("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"), GameDTO("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120")),
      IdentityCardDTO("33000010", "Marc Arndt"), EnvelopeDTO("44000013")))
    universalService.statusInformation("33000021") should equal(InformationDTO(Array(GameDTO("11000047", "Titel 3", "Autor 2", "Verlag 3", "12", "2 - 4", "90 - 120")), IdentityCardDTO("33000021"), EnvelopeDTO("44000024")))
    universalService.statusInformation("33000032") should equal(InformationDTO(Array(), IdentityCardDTO("33000032"), null))
    universalService.statusInformation("33000043") should equal(LendingEntityNotExistsDTO("33000043"))
    universalService.statusInformation("33000054") should equal(LendingEntityNotExistsDTO("33000054"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def envelopeStatusInformation(@ArquillianResteasyResource universalService: UniversalService): Unit = {
    universalService.statusInformation("44000013") should equal(InformationDTO(
      Array(GameDTO("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120"), GameDTO("11000025", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120"), GameDTO("11000036", "Titel 2", "Autor 1", "Verlag 2", "15", null, "90 - 120")),
      IdentityCardDTO("33000010", "Marc Arndt"), EnvelopeDTO("44000013")))
    universalService.statusInformation("44000024") should equal(InformationDTO(Array(GameDTO("11000047", "Titel 3", "Autor 2", "Verlag 3", "12", "2 - 4", "90 - 120")), IdentityCardDTO("33000021"), EnvelopeDTO("44000024")))
    universalService.statusInformation("44000035") should equal(InformationDTO(Array(), null, EnvelopeDTO("44000035")))
    universalService.statusInformation("44000046") should equal(LendingEntityNotExistsDTO("44000046"))
    universalService.statusInformation("44000057") should equal(LendingEntityNotExistsDTO("44000057"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def incorrectBarcodeStatusInformation(@ArquillianResteasyResource universalService: UniversalService): Unit = {
    universalService.statusInformation("11000013") should equal(IncorrectBarcodeDTO("11000013"))
    universalService.statusInformation("33000011") should equal(IncorrectBarcodeDTO("33000011"))
    universalService.statusInformation("44000012") should equal(IncorrectBarcodeDTO("44000012"))
    universalService.statusInformation("123") should equal(IncorrectBarcodeDTO("123"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def gamesInformation(@ArquillianResteasyResource universalService: UniversalService): Unit = {
    universalService.gamesInformation(GameInformationRequestDTO("Titel", null, null, null, null, null, null)).foundGames.length should be(6)
    universalService.gamesInformation(GameInformationRequestDTO("Titel 1", null, null, null, null, null, null)).foundGames.length should be(1)
    universalService.gamesInformation(GameInformationRequestDTO("Titel 2", null, null, null, null, null, null)).foundGames.length should be(2)
    universalService.gamesInformation(GameInformationRequestDTO("Autor 1", null, null, null, null, null, null)).foundGames.length should be(3)
    universalService.gamesInformation(GameInformationRequestDTO("Verlag 2", null, null, null, null, null, null)).foundGames.length should be(4)
  }
}