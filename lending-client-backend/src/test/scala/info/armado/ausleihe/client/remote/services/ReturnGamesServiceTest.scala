package info.armado.ausleihe.client.remote.services

import java.time.Year
import info.armado.ausleihe.client.transport.dataobjects.entities._
import info.armado.ausleihe.client.transport.dataobjects.inuse.NotInUseDTO
import info.armado.ausleihe.client.transport.requests.ReturnGameRequestDTO
import info.armado.ausleihe.client.transport.results._
import org.arquillian.ape.rdbms.{ShouldMatchDataSet, UsingDataSet}
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource
import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Matchers.{convertToAnyShouldWrapper, equal}
import org.scalatest.junit.JUnitSuite

object ReturnGamesServiceTest extends WebDeployment

@RunWith(classOf[Arquillian])
class ReturnGamesServiceTest extends JUnitSuite {

  /**
    * Checks that a currently borrowed game can be successfully returned.
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(
    value = Array("datasets/return-game.xml"),
    excludeColumns = Array("LENDGAME.RETURNTIME")
  )
  def successfulReturnGame(
      @ArquillianResteasyResource returnGamesService: ReturnGamesService
  ): Unit = {
    returnGamesService.returnGame(ReturnGameRequestDTO("11000014")) should equal(
      ReturnGameSuccessDTO(
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
      )
    )
  }

  /**
    * Checks that a currently not borrowed game can't be returned.
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notBorrowedReturnGame(
      @ArquillianResteasyResource returnGamesService: ReturnGamesService
  ): Unit = {
    returnGamesService.returnGame(ReturnGameRequestDTO("11000058")) should equal(
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

  }

  /**
    * Checks that a not activated game can't be returned
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notActivatedReturnGame(
      @ArquillianResteasyResource returnGamesService: ReturnGamesService
  ): Unit = {
    returnGamesService.returnGame(ReturnGameRequestDTO("11000070")) should equal(
      LendingEntityNotExistsDTO("11000070")
    )
  }

  /**
    * Checks that a not existing game can't be returned
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def notExistingReturnGame(
      @ArquillianResteasyResource returnGamesService: ReturnGamesService
  ): Unit = {
    returnGamesService.returnGame(ReturnGameRequestDTO("11000081")) should equal(
      LendingEntityNotExistsDTO("11000081")
    )
  }

  /**
    * Checks that an incorrect barcode can't be processed
    */
  @Test
  @UsingDataSet(Array("datasets/initial.xml"))
  @ShouldMatchDataSet(Array("datasets/initial.xml"))
  def incorrectBarcodeReturnGame(
      @ArquillianResteasyResource returnGamesService: ReturnGamesService
  ): Unit = {
    returnGamesService.returnGame(ReturnGameRequestDTO("11000013")) should equal(
      IncorrectBarcodeDTO("11000013")
    )
  }

}
