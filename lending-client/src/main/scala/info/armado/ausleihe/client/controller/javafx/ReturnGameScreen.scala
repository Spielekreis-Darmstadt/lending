package info.armado.ausleihe.client.controller.javafx

import info.armado.ausleihe.client.connection.RestServerConnection
import info.armado.ausleihe.client.controller.javafx.screen.ErrorScreen
import info.armado.ausleihe.client.controller.javafx.screen.FXMLLoadable
import info.armado.ausleihe.client.controller.javafx.screen.FatalityState
import info.armado.ausleihe.client.controller.javafx.screen.FatalityState.NonFatal
import info.armado.ausleihe.client.controller.javafx.screen.FatalityState.NonFatalInputReset
import info.armado.ausleihe.client.controller.javafx.screen.FatalityState.Reset
import info.armado.ausleihe.client.controller.javafx.screen.FocusRequestable
import info.armado.ausleihe.client.controller.javafx.screen.OtherGroupScreen
import info.armado.ausleihe.client.controller.javafx.screen.Resetable
import info.armado.ausleihe.client.controller.javafx.screen.Screen
import info.armado.ausleihe.client.model.Barcode
import info.armado.ausleihe.client.model.BarcodeTest
import info.armado.ausleihe.client.model.Configuration
import info.armado.ausleihe.client.model.WrongChecksum
import info.armado.ausleihe.client.model.WrongLength
import info.armado.ausleihe.remote.dataobjects.entities.GameData
import info.armado.ausleihe.remote.dataobjects.inuse.NotInUse
import info.armado.ausleihe.remote.results.AbstractResult
import info.armado.ausleihe.remote.results.IncorrectBarcode
import info.armado.ausleihe.remote.results.LendingEntityInUse
import info.armado.ausleihe.remote.results.LendingEntityNotExists
import info.armado.ausleihe.remote.results.ReturnGameSuccess
import javafx.beans.binding.Bindings
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import scalafx.Includes.eventClosureWrapperWithParam
import scalafx.Includes.jfxActionEvent2sfx
import scalafx.Includes.jfxFlowPane2sfx
import scalafx.Includes.jfxLabel2sfx
import scalafx.Includes.jfxObjectProperty2sfx
import scalafx.Includes.jfxStackPane2sfx
import scalafx.Includes.jfxTableView2sfx
import scalafx.Includes.jfxTextField2sfx
import scalafx.beans.property.StringProperty.sfxStringProperty2jfx
import scalafx.collections.ObservableBuffer
import scalafx.collections.ObservableBuffer.observableBuffer2ObservableList
import scalafx.event.ActionEvent
import info.armado.ausleihe.client.controller.javafx.screen.FunctionScreen

class ReturnGameScreen extends StackPane with Screen with FunctionScreen {
  val GamePrefix = Configuration.gamePrefix
  val OtherGroupPrefix = Configuration.otherGamePrefix

  val scannedGames: ObservableBuffer[GameData] = new ObservableBuffer[GameData]()

  val defaultTaskMessage = "Scannen Sie ein Spiel um dieses Spiel zurückzunehmen!"
  val barcodePromptMessage = "Spielebarcode"

  val inputScreen: InputScreen = new InputScreen
  val errorScreen: ErrorScreen = new ErrorScreen
  val otherGroupScreen: OtherGroupScreen = new OtherGroupScreen

  var currentScreen: Pane with FocusRequestable = _

  loadFXML("javafx/window.fxml")

  @FXML
  def initialize(): Unit = {
    this.children.add(errorScreen)
    this.children.add(otherGroupScreen)
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

  def activateOtherGroup(message: String) = {
    otherGroupScreen.message = message
    otherGroupScreen.lastScreen = currentScreen

    activeScreen(otherGroupScreen)
  }

  def deactivateError() = activeScreen(errorScreen.lastScreen)

  def deactivateOtherGroupScreen() = activeScreen(otherGroupScreen.lastScreen)

  def askForFocus(): Unit = Option(currentScreen) match {
    case Some(_: InputScreen) => {
      currentScreen.askForFocus()
    }
    case _ =>
  }

  def totalReset(): Unit = {
    scannedGames.clear()

    inputScreen.reset()

    activeScreen(inputScreen)
  }

  def reset(): Unit = currentScreen match {
    case ErrorScreen(_, _, Some(NonFatal)) => deactivateError()
    case ErrorScreen(_, _, Some(NonFatalInputReset)) => {
      deactivateError()
      inputScreen.barcodeTextField.text = ""
    }
    case ErrorScreen(_, _, Some(Reset)) | OtherGroupScreen(_, _) | _ => totalReset()
  }

  class InputScreen extends BorderPane with FXMLLoadable with InputFunctionScreen[AbstractResult] {
    @FXML var taskLabel: Label = _

    @FXML var barcodeTextField: TextField = _

    @FXML var contentFlowPane: FlowPane = _

    var contentPanel: ContentPanel = _

    // load fxml file
    this.loadFXML("javafx/borrow.fxml")

    @FXML
    def initialize(): Unit = {
      taskLabel.text = defaultTaskMessage
      barcodeTextField.promptText = barcodePromptMessage
      barcodeTextField.onAction = (event: ActionEvent) => currentScreen match {
        case _: InputScreen => processBarcode(barcodeTextField.text.get)
        case _ =>
      }

      contentPanel = new ContentPanel
      contentFlowPane.children.add(contentPanel)
    }

    def processBarcode(input: String): Unit = BarcodeTest(input) match {
      // the barcode is either to short or to long
      case wrongLength @ WrongLength(barcode) => showEvent(WrongLengthEvent(wrongLength))

      // the barcode has the wrong checksum
      case wrongChecksum @ WrongChecksum(barcode) => showEvent(WrongChecksumEvent(wrongChecksum))

      // try to return the game with the scanned id
      case Barcode(GamePrefix, counter, checksum) => tryResult(RestServerConnection.returnGame(input))

      // the game belongs to the other group
      case Barcode(OtherGroupPrefix, counter, checksum) => showEvent(OtherGroupGameEvent(input))

      // something strange was scanned
      case _ => {
        activateError(s"""Barcode $input ist für die Funktion ungültig bzw. nicht vorhanden.""", Reset)
      }
    }

    def showEvent(event: Event): Unit = event match {
      case WrongLengthEvent(_) => {
        activateError("Der Barcode hat die falsche Länge.", NonFatal)
      }

      case WrongChecksumEvent(_) => {
        activateError("Die Prüfsumme des Barcodes ist nicht korrekt!", NonFatal)
      }

      case OtherGroupGameEvent(barcode) => {
        activateOtherGroup(s"Das gescannte Spiel $barcode gehört zur anderen Gruppe, bitte Ausleiher dort hin verweisen!")
      }

      case other @ _ => Console.err.println(s"Found a strange event: $other")
    }

    def showResult(result: AbstractResult): Unit = result match {
      case IncorrectBarcode(text) => {
        activateError(s"""Der Barcode "$text" ist nicht valide.""", NonFatal)
      }

      case LendingEntityNotExists(barcode) => {
        activateError(s"""Der Barcode "$barcode" ist für die Funktion entweder ungültig oder nicht vorhanden.""", NonFatal)
      }

      case LendingEntityInUse(GameData(barcode, title, author, publisher, _, _, _), NotInUse()) => {
        activateError(s"""Das Spiel "$barcode" mit dem Titel $title wurde nicht ausgeliehen!""", NonFatalInputReset)
      }

      case ReturnGameSuccess(game @ GameData(barcode, title, author, publisher, _, _, _)) => {
        scannedGames += game

        taskLabel.text = "Scannen Sie ein Spiel um dieses Spiel zurückzunehmen!"
        barcodeTextField.text = ""
      }
    }

    def askForFocus(): Unit = barcodeTextField.requestFocus()

    def reset(): Unit = {
      taskLabel.text = defaultTaskMessage
      barcodeTextField.text = ""
    }
  }

  class ContentPanel extends VBox with FXMLLoadable {
    @FXML
    protected var gamesTableView: TableView[GameData] = _

    this.loadFXML("javafx/return-games-information.fxml")

    @FXML
    def initialize(): Unit = {
      this.visibleProperty().bind(Bindings.isNotEmpty(scannedGames))

      gamesTableView.items = scannedGames
      gamesTableView.items.onChange(scannedGames.lastOption match {
        case Some(lastGame) => gamesTableView.scrollTo(lastGame)
        case None =>
      })
    }
  }
}