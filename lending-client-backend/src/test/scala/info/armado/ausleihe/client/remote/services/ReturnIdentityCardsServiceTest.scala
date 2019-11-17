package info.armado.ausleihe.client.remote.services

import info.armado.ausleihe.client.transport.dataobjects.entities._
import info.armado.ausleihe.client.transport.dataobjects.inuse.NotInUseDTO
import info.armado.ausleihe.client.transport.requests.ReturnIdentityCardRequestDTO
import info.armado.ausleihe.client.transport.results._
import org.arquillian.ape.rdbms.{ShouldMatchDataSet, UsingDataSet}
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource
import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.{convertToAnyShouldWrapper, equal}
import org.scalatest.junit.JUnitSuite

object ReturnIdentityCardsServiceTest extends WebDeployment

@RunWith(classOf[Arquillian])
class ReturnIdentityCardsServiceTest extends JUnitSuite {
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(
    value = Array("datasets/return-identity-card.xml"),
    excludeColumns = Array("LENDIDENTITYCARD.RETURNTIME")
  )
  def successfulReturnIdentityCard(
      @ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCardsService
  ): Unit = {
    returnIdentityCardsService.returnIdentityCard(
      ReturnIdentityCardRequestDTO("33000101", "44000104")
    ) should equal(
      ReturnIdentityCardSuccessDTO(IdentityCardDTO("33000101"), EnvelopeDTO("44000104"))
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def identityCardHasBorrowedGamesReturnIdentityCard(
      @ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCardsService
  ): Unit = {
    returnIdentityCardsService.returnIdentityCard(
      ReturnIdentityCardRequestDTO("33000010", "44000013")
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

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def identityCardDoesNotBelongToEnvelopeReturnIdentityCard(
      @ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCardsService
  ): Unit = {
    returnIdentityCardsService.returnIdentityCard(
      ReturnIdentityCardRequestDTO("33000101", "44000013")
    ) should equal(
      IdentityCardEnvelopeNotBoundDTO(
        IdentityCardDTO("33000101"),
        EnvelopeDTO("44000013"),
        EnvelopeDTO("44000104")
      )
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def identityCardNotIssuedReturnIdentityCard(
      @ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCardsService
  ): Unit = {
    returnIdentityCardsService.returnIdentityCard(
      ReturnIdentityCardRequestDTO("33000032", "44000013")
    ) should equal(LendingEntityInUseDTO(IdentityCardDTO("33000032"), NotInUseDTO()))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def envelopeNotIssuedReturnIdentityCard(
      @ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCardsService
  ): Unit = {
    returnIdentityCardsService.returnIdentityCard(
      ReturnIdentityCardRequestDTO("33000010", "44000035")
    ) should equal(LendingEntityInUseDTO(EnvelopeDTO("44000035"), NotInUseDTO()))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def identityCardNotActivatedReturnIdentityCard(
      @ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCardsService
  ): Unit = {
    returnIdentityCardsService.returnIdentityCard(
      ReturnIdentityCardRequestDTO("33000043", "44000013")
    ) should equal(LendingEntityNotExistsDTO("33000043"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def envelopeNotActivatedReturnIdentityCard(
      @ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCardsService
  ): Unit = {
    returnIdentityCardsService.returnIdentityCard(
      ReturnIdentityCardRequestDTO("33000010", "44000046")
    ) should equal(LendingEntityNotExistsDTO("44000046"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def identityCardNotExistsReturnIdentityCard(
      @ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCardsService
  ): Unit = {
    returnIdentityCardsService.returnIdentityCard(
      ReturnIdentityCardRequestDTO("33000054", "44000013")
    ) should equal(LendingEntityNotExistsDTO("33000054"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def envelopeNotExistsReturnIdentityCard(
      @ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCardsService
  ): Unit = {
    returnIdentityCardsService.returnIdentityCard(
      ReturnIdentityCardRequestDTO("33000010", "44000057")
    ) should equal(LendingEntityNotExistsDTO("44000057"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def incorrectIdentityCardBarcodeReturnIdentityCard(
      @ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCardsService
  ): Unit = {
    returnIdentityCardsService.returnIdentityCard(
      ReturnIdentityCardRequestDTO("33000011", "44000104")
    ) should equal(IncorrectBarcodeDTO("33000011"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def incorrectEnvelopeBarcodeReturnIdentityCard(
      @ArquillianResteasyResource returnIdentityCardsService: ReturnIdentityCardsService
  ): Unit = {
    returnIdentityCardsService.returnIdentityCard(
      ReturnIdentityCardRequestDTO("33000101", "44000014")
    ) should equal(IncorrectBarcodeDTO("44000014"))
  }
}
