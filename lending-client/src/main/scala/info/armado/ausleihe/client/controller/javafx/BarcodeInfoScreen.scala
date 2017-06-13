package info.armado.ausleihe.client.controller.javafx

import info.armado.ausleihe.client.connection.RestServerConnection
import info.armado.ausleihe.client.controller.javafx.screen.ErrorScreen
import info.armado.ausleihe.client.controller.javafx.screen.FXMLLoadable
import info.armado.ausleihe.client.controller.javafx.screen.FatalityState
import info.armado.ausleihe.client.controller.javafx.screen.FatalityState.NonFatal
import info.armado.ausleihe.client.controller.javafx.screen.FatalityState.NonFatalInputReset
import info.armado.ausleihe.client.controller.javafx.screen.FatalityState.Reset
import info.armado.ausleihe.client.controller.javafx.screen.FocusRequestable
import info.armado.ausleihe.client.controller.javafx.screen.Resetable
import info.armado.ausleihe.client.controller.javafx.screen.Screen
import info.armado.ausleihe.client.model.Barcode
import info.armado.ausleihe.client.model.BarcodeTest
import info.armado.ausleihe.client.model.WrongChecksum
import info.armado.ausleihe.client.model.WrongLength
import info.armado.ausleihe.remote.dataobjects.entities.GameData
import info.armado.ausleihe.remote.results.AbstractResult
import info.armado.ausleihe.remote.results.IncorrectBarcode
import info.armado.ausleihe.remote.results.Information
import info.armado.ausleihe.remote.results.LendingEntityNotExists
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import scalafx.Includes._
import scalafx.beans.property.StringProperty.sfxStringProperty2jfx
import scalafx.event.ActionEvent
import javafx.beans.property.SimpleObjectProperty
import info.armado.ausleihe.client.controller.javafx.screen.FunctionScreen
import info.armado.ausleihe.remote.dataobjects.entities.IdentityCardData
import info.armado.ausleihe.remote.dataobjects.entities.EnvelopeData
import info.armado.ausleihe.client.controller.javafx.screen.InputScreen

class BarcodeInfoScreen extends StackPane with Screen with FunctionScreen {
  val searchResult = new SimpleObjectProperty[Information](null)

  val inputScreen = new BarcodeInputScreen
  val outputScreen = new BarcodeOutputScreen
  val errorScreen = new ErrorScreen

  var currentScreen: Pane with FocusRequestable = inputScreen

  loadFXML("javafx/window.fxml")

  @FXML
  def initialize(): Unit = {
    this.children.add(errorScreen)
    this.children.add(outputScreen)
    this.children.add(inputScreen)

    activeScreen(inputScreen)
  }

  def activeScreen(screen: Pane with FocusRequestable) = {
    screen.toFront()
    currentScreen = screen
  }

  def activateError(message: String, fatalityState: FatalityState) = {
    errorScreen.message = message
    errorScreen.fatalityState = fatalityState
    errorScreen.lastScreen = currentScreen

    activeScreen(errorScreen)
  }

  def deactivateError() = {
    activeScreen(errorScreen.lastScreen)
  }

  def askForFocus(): Unit = Option(currentScreen) match {
    case Some(_: BarcodeInputScreen) => {
      currentScreen.askForFocus()
    }
    case _ =>
  }

  def totalReset() = {
    inputScreen.reset()
    activeScreen(inputScreen)
  }

  def reset(): Unit = currentScreen match {
    case ErrorScreen(_, _, Some(NonFatal)) => deactivateError()
    case ErrorScreen(_, _, Some(NonFatalInputReset)) => {
      deactivateError()
      inputScreen.clearBarcodeInput()
    }
    case ErrorScreen(_, _, Some(Reset)) | _ => totalReset()
  }

  class BarcodeInputScreen extends BorderPane with FXMLLoadable with InputFunctionScreen[AbstractResult] {
    @FXML protected var taskLabel: Label = _

    @FXML protected var barcodeTextField: TextField = _

    loadFXML("javafx/borrow.fxml")

    @FXML
    def initialize(): Unit = {
      barcodeTextField.onAction = (event: ActionEvent) => currentScreen match {
        case _: InputScreen => processBarcode(barcodeTextField.text.get)
        case _ =>
      }

      reset()
    }

    def clearBarcodeInput(): Unit = barcodeTextField.text = ""

    def processBarcode(input: String): Unit = BarcodeTest(input) match {
      case WrongLength(_) => {
        //background = Color.RED
        taskLabel.text = "Scannen Sie einen Ausweis, Umschlag oder Spiel zur Statusanzeige"
        //this.result = "Der Barcode hat die falsche Länge."
        activateError("Der Barcode hat die falsche Länge.", NonFatal)
      }

      case WrongChecksum(_) => {
        //background = Color.RED
        taskLabel.text = "Scannen Sie einen Ausweis, Umschlag oder Spiel zur Statusanzeige"
        //this.result = "Die Prüfsumme des Barcodes ist nicht korrekt!"
        activateError("Die Prüfsumme des Barcodes ist nicht korrekt!", NonFatal)
      }

      case Barcode(prefix, counter, checksum) => tryResult(RestServerConnection.getBarcodeStatus(input))

      // something totally different was scanned that couldn't be processed before
      case _ => {
        activateError(s"""Der Barcode $input ist für die Funktion ungültig bzw. nicht vorhanden.""", Reset)
      }
    }

    def showResult(result: AbstractResult): Unit = result match {
      case IncorrectBarcode(barcode) => {
        //background = Color.RED
        taskLabel.text = "Scannen Sie einen Ausweis, Umschlag oder Spiel zur Statusanzeige"
        //this.result = s"Barcode $barcode ist für die Funktion ungültig bzw. nicht vorhanden."
        activateError(s"Barcode $barcode ist für die Funktion ungültig bzw. nicht vorhanden.", Reset)
      }
      case LendingEntityNotExists(barcode) => {
        //background = Color.RED
        taskLabel.text = "Scannen Sie einen Ausweis, Umschlag oder Spiel zur Statusanzeige"
        //this.result = s"Barcode $barcode ist für die Funktion ungültig bzw. nicht vorhanden."
        activateError(s"Barcode $barcode ist für die Funktion ungültig bzw. nicht vorhanden.", Reset)
      }
      case information @ Information(_, _, _) => {
        searchResult.value = information
        activeScreen(outputScreen)
      }
    }

    def reset(): Unit = {
      taskLabel.text = "Geben sie bitte den Barcode eines Spiels, Ausweises oder Umschlags ein!"
      barcodeTextField.promptText = "Spiel-/Ausweis-/Umschlagbarcode"
      barcodeTextField.text = ""
      askForFocus()
    }

    def askForFocus(): Unit = barcodeTextField.requestFocus()
  }

  class BarcodeOutputScreen extends BorderPane with FXMLLoadable with BlankOutputFunctionScreen {
    @FXML protected var identityCardLabel: Label = _

    @FXML protected var gamesTableView: TableView[GameData] = _

    @FXML protected var envelopeLabel: Label = _

    loadFXML("javafx/testing-result.fxml")

    @FXML
    def initialize(): Unit = {
      searchResult.onChange((observableValue, oldValue, newValue) => Option(newValue) match {
        case Some(Information(games, identityCard, envelope)) => {
          gamesTableView.getItems.setAll(games: _*)

          identityCardLabel.text = Option(identityCard) match {
            case Some(IdentityCardData(barcode, None)) => s"Ausweis: $barcode"
            case Some(IdentityCardData(barcode, Some(owner))) => s"Ausweis: $barcode ($owner)"
            case None => "Kein Ausweis gefunden"
          }

          envelopeLabel.text = Option(envelope) match {
            case Some(EnvelopeData(barcode)) => s"Umschlag: $barcode"
            case None => "Kein Umschlag gefunden"
          }
        }
        case None =>
      })
    }
  }
}