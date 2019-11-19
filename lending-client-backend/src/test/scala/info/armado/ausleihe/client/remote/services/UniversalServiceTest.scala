package info.armado.ausleihe.client.remote.services

import java.time.Year
import info.armado.ausleihe.client.transport.dataobjects.entities._
import info.armado.ausleihe.client.transport.dataobjects.inuse._
import info.armado.ausleihe.client.transport.requests.GameInformationRequestDTO
import info.armado.ausleihe.client.transport.results._
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
    universalService.barcodeInUse("11000014") should equal(
      LendingEntityInUseDTO(
        GameDTO(
          "11000014",
          "Titel 1",
          "Autor 1",
          "Verlag 1",
          Year.of(12),
          PlayerCountDTO(2),
          DurationDTO(90, 120),
          Year.of(2016)
        ),
        GameInUseDTO(IdentityCardDTO("33000010", "Marc Arndt"), EnvelopeDTO("44000013"))
      )
    )
    universalService.barcodeInUse("11000058") should equal(
      LendingEntityInUseDTO(
        GameDTO(
          "11000058",
          "Titel 4",
          "Autor 3",
          "Verlag 2",
          Year.of(13),
          PlayerCountDTO(3, 5),
          DurationDTO(90, 120),
          Year.of(2016)
        ),
        NotInUseDTO()
      )
    )
    // test if an not activated game can't be found
    universalService.barcodeInUse("11000070") should equal(LendingEntityNotExistsDTO("11000070"))
    // test if a not existing game can't be found
    universalService.barcodeInUse("11000081") should equal(LendingEntityNotExistsDTO("11000081"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def identityCardBarcodeInUse(
      @ArquillianResteasyResource universalService: UniversalService
  ): Unit = {
    universalService.barcodeInUse("33000010") should equal(
      LendingEntityInUseDTO(
        IdentityCardDTO("33000010", "Marc Arndt"),
        IdentityCardInUseDTO(
          EnvelopeDTO("44000013"),
          Array(
            GameDTO(
              "11000014",
              "Titel 1",
              "Autor 1",
              "Verlag 1",
              Year.of(12),
              PlayerCountDTO(2),
              DurationDTO(90, 120),
              Year.of(2016)
            ),
            GameDTO(
              "11000025",
              "Titel 2",
              "Autor 1",
              "Verlag 2",
              Year.of(15),
              null,
              DurationDTO(90, 120),
              null
            ),
            GameDTO(
              "11000036",
              "Titel 2",
              "Autor 1",
              "Verlag 2",
              Year.of(15),
              null,
              DurationDTO(90, 120),
              Year.of(2015)
            )
          )
        )
      )
    )
    universalService.barcodeInUse("33000032") should equal(
      LendingEntityInUseDTO(IdentityCardDTO("33000032"), NotInUseDTO())
    )
    universalService.barcodeInUse("33000043") should equal(LendingEntityNotExistsDTO("33000043"))
    universalService.barcodeInUse("33000054") should equal(LendingEntityNotExistsDTO("33000054"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def envelopeBarcodeInUse(@ArquillianResteasyResource universalService: UniversalService): Unit = {
    universalService.barcodeInUse("44000013") should equal(
      LendingEntityInUseDTO(
        EnvelopeDTO("44000013"),
        EnvelopeInUseDTO(
          IdentityCardDTO("33000010", "Marc Arndt"),
          Array(
            GameDTO(
              "11000014",
              "Titel 1",
              "Autor 1",
              "Verlag 1",
              Year.of(12),
              PlayerCountDTO(2),
              DurationDTO(90, 120),
              Year.of(2016)
            ),
            GameDTO(
              "11000025",
              "Titel 2",
              "Autor 1",
              "Verlag 2",
              Year.of(15),
              null,
              DurationDTO(90, 120),
              null
            ),
            GameDTO(
              "11000036",
              "Titel 2",
              "Autor 1",
              "Verlag 2",
              Year.of(15),
              null,
              DurationDTO(90, 120),
              Year.of(2015)
            )
          )
        )
      )
    )
    universalService.barcodeInUse("44000035") should equal(
      LendingEntityInUseDTO(EnvelopeDTO("44000035"), NotInUseDTO())
    )
    universalService.barcodeInUse("44000046") should equal(LendingEntityNotExistsDTO("44000046"))
    universalService.barcodeInUse("44000057") should equal(LendingEntityNotExistsDTO("44000057"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def incorrectBarcodeInUse(
      @ArquillianResteasyResource universalService: UniversalService
  ): Unit = {
    universalService.barcodeInUse("11000013") should equal(IncorrectBarcodeDTO("11000013"))
    universalService.barcodeInUse("33000011") should equal(IncorrectBarcodeDTO("33000011"))
    universalService.barcodeInUse("44000012") should equal(IncorrectBarcodeDTO("44000012"))
    universalService.barcodeInUse("123") should equal(IncorrectBarcodeDTO("123"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def gamesStatusInformation(
      @ArquillianResteasyResource universalService: UniversalService
  ): Unit = {
    universalService.statusInformation("11000014") should equal(
      InformationDTO(
        Array(
          GameDTO(
            "11000014",
            "Titel 1",
            "Autor 1",
            "Verlag 1",
            Year.of(12),
            PlayerCountDTO(2),
            DurationDTO(90, 120),
            Year.of(2016)
          )
        ),
        IdentityCardDTO("33000010", "Marc Arndt"),
        EnvelopeDTO("44000013")
      )
    )
    universalService.statusInformation("11000025") should equal(
      InformationDTO(
        Array(
          GameDTO(
            "11000025",
            "Titel 2",
            "Autor 1",
            "Verlag 2",
            Year.of(15),
            null,
            DurationDTO(90, 120),
            null
          )
        ),
        IdentityCardDTO("33000010", "Marc Arndt"),
        EnvelopeDTO("44000013")
      )
    )
    universalService.statusInformation("11000047") should equal(
      InformationDTO(
        Array(
          GameDTO(
            "11000047",
            "Titel 3",
            "Autor 2",
            "Verlag 3",
            Year.of(12),
            PlayerCountDTO(2, 4),
            DurationDTO(90, 120),
            Year.of(2016)
          )
        ),
        IdentityCardDTO("33000021"),
        EnvelopeDTO("44000024")
      )
    )
    universalService.statusInformation("11000058") should equal(
      InformationDTO(
        Array(
          GameDTO(
            "11000058",
            "Titel 4",
            "Autor 3",
            "Verlag 2",
            Year.of(13),
            PlayerCountDTO(3, 5),
            DurationDTO(90, 120),
            Year.of(2016)
          )
        ),
        null,
        null
      )
    )
    universalService.statusInformation("11000069") should equal(
      InformationDTO(
        Array(
          GameDTO(
            "11000069",
            "Titel 4",
            "Autor 3",
            "Verlag 2",
            Year.of(13),
            PlayerCountDTO(3, 5),
            DurationDTO(90, 120),
            Year.of(2016)
          )
        ),
        null,
        null
      )
    )
    // test if an not activated game can't be found
    universalService.statusInformation("11000070") should equal(
      LendingEntityNotExistsDTO("11000070")
    )
    // test if a not existing game can't be found
    universalService.statusInformation("11000081") should equal(
      LendingEntityNotExistsDTO("11000081")
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def identityCardStatusInformation(
      @ArquillianResteasyResource universalService: UniversalService
  ): Unit = {
    universalService.statusInformation("33000010") should equal(
      InformationDTO(
        Array(
          GameDTO(
            "11000014",
            "Titel 1",
            "Autor 1",
            "Verlag 1",
            Year.of(12),
            PlayerCountDTO(2),
            DurationDTO(90, 120),
            Year.of(2016)
          ),
          GameDTO(
            "11000025",
            "Titel 2",
            "Autor 1",
            "Verlag 2",
            Year.of(15),
            null,
            DurationDTO(90, 120),
            null
          ),
          GameDTO(
            "11000036",
            "Titel 2",
            "Autor 1",
            "Verlag 2",
            Year.of(15),
            null,
            DurationDTO(90, 120),
            Year.of(2015)
          )
        ),
        IdentityCardDTO("33000010", "Marc Arndt"),
        EnvelopeDTO("44000013")
      )
    )
    universalService.statusInformation("33000021") should equal(
      InformationDTO(
        Array(
          GameDTO(
            "11000047",
            "Titel 3",
            "Autor 2",
            "Verlag 3",
            Year.of(12),
            PlayerCountDTO(2, 4),
            DurationDTO(90, 120),
            Year.of(2016)
          )
        ),
        IdentityCardDTO("33000021"),
        EnvelopeDTO("44000024")
      )
    )
    universalService.statusInformation("33000032") should equal(
      InformationDTO(Array(), IdentityCardDTO("33000032"), null)
    )
    universalService.statusInformation("33000043") should equal(
      LendingEntityNotExistsDTO("33000043")
    )
    universalService.statusInformation("33000054") should equal(
      LendingEntityNotExistsDTO("33000054")
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def envelopeStatusInformation(
      @ArquillianResteasyResource universalService: UniversalService
  ): Unit = {
    universalService.statusInformation("44000013") should equal(
      InformationDTO(
        Array(
          GameDTO(
            "11000014",
            "Titel 1",
            "Autor 1",
            "Verlag 1",
            Year.of(12),
            PlayerCountDTO(2),
            DurationDTO(90, 120),
            Year.of(2016)
          ),
          GameDTO(
            "11000025",
            "Titel 2",
            "Autor 1",
            "Verlag 2",
            Year.of(15),
            null,
            DurationDTO(90, 120),
            null
          ),
          GameDTO(
            "11000036",
            "Titel 2",
            "Autor 1",
            "Verlag 2",
            Year.of(15),
            null,
            DurationDTO(90, 120),
            Year.of(2015)
          )
        ),
        IdentityCardDTO("33000010", "Marc Arndt"),
        EnvelopeDTO("44000013")
      )
    )
    universalService.statusInformation("44000024") should equal(
      InformationDTO(
        Array(
          GameDTO(
            "11000047",
            "Titel 3",
            "Autor 2",
            "Verlag 3",
            Year.of(12),
            PlayerCountDTO(2, 4),
            DurationDTO(90, 120),
            Year.of(2016)
          )
        ),
        IdentityCardDTO("33000021"),
        EnvelopeDTO("44000024")
      )
    )
    universalService.statusInformation("44000035") should equal(
      InformationDTO(Array(), null, EnvelopeDTO("44000035"))
    )
    universalService.statusInformation("44000046") should equal(
      LendingEntityNotExistsDTO("44000046")
    )
    universalService.statusInformation("44000057") should equal(
      LendingEntityNotExistsDTO("44000057")
    )
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def incorrectBarcodeStatusInformation(
      @ArquillianResteasyResource universalService: UniversalService
  ): Unit = {
    universalService.statusInformation("11000013") should equal(IncorrectBarcodeDTO("11000013"))
    universalService.statusInformation("33000011") should equal(IncorrectBarcodeDTO("33000011"))
    universalService.statusInformation("44000012") should equal(IncorrectBarcodeDTO("44000012"))
    universalService.statusInformation("123") should equal(IncorrectBarcodeDTO("123"))
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def searchGamesByTitle(@ArquillianResteasyResource universalService: UniversalService): Unit = {
    universalService
      .gamesInformation(
        GameInformationRequestDTO("Titel", null, null, null, null, null, null, null)
      )
      .foundGames
      .length should be(6)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, "Titel", null, null, null, null, null, null)
      )
      .foundGames
      .length should be(6)

    universalService
      .gamesInformation(
        GameInformationRequestDTO("Titel 1", null, null, null, null, null, null, null)
      )
      .foundGames
      .length should be(1)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, "Titel 1", null, null, null, null, null, null)
      )
      .foundGames
      .length should be(1)
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def searchGamesByAuthor(@ArquillianResteasyResource universalService: UniversalService): Unit = {
    universalService
      .gamesInformation(
        GameInformationRequestDTO("Autor 1", null, null, null, null, null, null, null)
      )
      .foundGames
      .length should be(3)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, null, "Autor 1", null, null, null, null, null)
      )
      .foundGames
      .length should be(3)
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def searchGamesByPublisher(
      @ArquillianResteasyResource universalService: UniversalService
  ): Unit = {
    universalService
      .gamesInformation(
        GameInformationRequestDTO("Verlag 2", null, null, null, null, null, null, null)
      )
      .foundGames
      .length should be(4)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, null, null, "Verlag 2", null, null, null, null)
      )
      .foundGames
      .length should be(4)
  }

  @Test
  @UsingDataSet(Array("datasets/search-player-count.xml"))
  @ShouldMatchDataSet(Array("datasets/search-player-count.xml"))
  def searchGamesByPlayerCount(
      @ArquillianResteasyResource universalService: UniversalService
  ): Unit = {
    // should find all games with either: Title 3, Author 3 and Publisher 3
    universalService
      .gamesInformation(
        GameInformationRequestDTO("3", null, null, null, null, null, null, null)
      )
      .foundGames
      .length should be(3)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, null, null, null, 1, null, null, null)
      )
      .foundGames
      .length should be(3)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, null, null, null, 2, null, null, null)
      )
      .foundGames
      .length should be(5)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, null, null, null, 3, null, null, null)
      )
      .foundGames
      .length should be(5)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, null, null, null, 4, null, null, null)
      )
      .foundGames
      .length should be(4)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, null, null, null, 5, null, null, null)
      )
      .foundGames
      .length should be(3)
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def searchGamesByMinimumAge(
      @ArquillianResteasyResource universalService: UniversalService
  ): Unit = {
    universalService
      .gamesInformation(
        GameInformationRequestDTO("13", null, null, null, null, null, null, null)
      )
      .foundGames
      .length should be(0)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, null, null, null, null, 13, null, null)
      )
      .foundGames
      .length should be(4)
  }

  @Test
  @UsingDataSet(Array("datasets/search-duration.xml"))
  @ShouldMatchDataSet(Array("datasets/search-duration.xml"))
  def searchGamesByDuration(
      @ArquillianResteasyResource universalService: UniversalService
  ): Unit = {
    universalService
      .gamesInformation(
        GameInformationRequestDTO("100", null, null, null, null, null, null, null)
      )
      .foundGames
      .length should be(0)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, null, null, null, null, null, 80, null)
      )
      .foundGames
      .length should be(2)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, null, null, null, null, null, 90, null)
      )
      .foundGames
      .length should be(4)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, null, null, null, null, null, 100, null)
      )
      .foundGames
      .length should be(5)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, null, null, null, null, null, 110, null)
      )
      .foundGames
      .length should be(5)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, null, null, null, null, null, 120, null)
      )
      .foundGames
      .length should be(5)
  }

  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def searchGamesByReleaseYear(
      @ArquillianResteasyResource universalService: UniversalService
  ): Unit = {
    universalService
      .gamesInformation(
        GameInformationRequestDTO("2015", null, null, null, null, null, null, null)
      )
      .foundGames
      .length should be(0)

    universalService
      .gamesInformation(
        GameInformationRequestDTO(null, null, null, null, null, null, null, 2015)
      )
      .foundGames
      .length should be(1)
  }
}
