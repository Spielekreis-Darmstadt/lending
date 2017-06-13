package info.armado.ausleihe.remote

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitSuite
import org.jboss.arquillian.junit.Arquillian
import javax.inject.Inject
import org.jboss.arquillian.persistence.UsingDataSet
import org.junit.Test
import org.scalatest.Matchers.convertToAnyShouldWrapper
import org.scalatest.Matchers.equal
import info.armado.ausleihe.remote.requests.ReturnGameRequest
import info.armado.ausleihe.remote.dataobjects.entities.GameData
import info.armado.ausleihe.remote.results.ReturnGameSuccess
import info.armado.ausleihe.remote.results.LendingEntityInUse
import info.armado.ausleihe.remote.dataobjects.inuse.NotInUse
import info.armado.ausleihe.remote.results.LendingEntityNotExists
import info.armado.ausleihe.remote.results.IncorrectBarcode

object ReturnGamesTest extends WebDeployment

@RunWith(classOf[Arquillian])
class ReturnGamesTest extends JUnitSuite {
  @Inject var returnGamesService: ReturnGames = _

  /**
   * Checks that a currently borrowed game can be successfully returned.
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def successfulReturnGame(): Unit = {
    returnGamesService.returnGame(ReturnGameRequest("11000014")) should equal(ReturnGameSuccess(GameData("11000014", "Titel 1", "Autor 1", "Verlag 1", "12", "2", "90 - 120")))
  }

  /**
   * Checks that a currently not borrowed game can't be returned.
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def notBorrowedReturnGame(): Unit = {
    returnGamesService.returnGame(ReturnGameRequest("11000058")) should equal(LendingEntityInUse(GameData("11000058", "Titel 4", "Autor 3", "Verlag 2", "13", "3 - 5", "90 - 120"), NotInUse()))

  }

  /**
   * Checks that a not activated game can't be returned
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def notActivatedReturnGame(): Unit = {
    returnGamesService.returnGame(ReturnGameRequest("11000070")) should equal(LendingEntityNotExists("11000070"))
  }

  /**
   * Checks that a not existing game can't be returned
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def notExistingReturnGame(): Unit = {
    returnGamesService.returnGame(ReturnGameRequest("11000081")) should equal(LendingEntityNotExists("11000081"))
  }

  /**
   * Checks that an incorrect barcode can't be processed
   */
  @Test
  @UsingDataSet(Array("datasets/full.xml"))
  def incorrectBarcodeReturnGame(): Unit = {
    returnGamesService.returnGame(ReturnGameRequest("11000013")) should equal(IncorrectBarcode("11000013"))
  }

}