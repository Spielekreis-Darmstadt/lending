package info.armado.ausleihe.client.controller.javafx

import info.armado.ausleihe.client.connection.RestServerConnection
import info.armado.ausleihe.client.controller.javafx.screen.FatalityState.{NonFatal, NonFatalInputReset, Reset}
import info.armado.ausleihe.client.controller.javafx.screen._
import info.armado.ausleihe.client.model._
import info.armado.ausleihe.client.transport.dataobjects.entities._
import info.armado.ausleihe.client.transport.dataobjects.inuse.NotInUseDTO
import info.armado.ausleihe.client.transport.results._
import javafx.beans.binding.Bindings
import javafx.fxml.FXML
import javafx.scene.control.{Label, TableView, TextField}
import javafx.scene.layout._
import scalafx.Includes._
import scalafx.beans.property.StringProperty.sfxStringProperty2jfx
import scalafx.collections.ObservableBuffer
import scalafx.collections.ObservableBuffer.observableBuffer2ObservableList
import scalafx.event.ActionEvent

class ReturnGameScreen extends StackPane with Screen with FunctionScreen {
  val GamePrefix: String = Configuration.gamePrefix
  val OtherGroupPrefix: String = Configuration.otherGamePrefix

  val scannedGames: ObservableBuffer[GameDTO] = new ObservableBuffer[GameDTO]()

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

  def activeScreen(screen: Pane with FocusRequestable): Unit = {
    screen.toFront()
    currentScreen = screen
  }

  def activateError(message: String, fatalityState: FatalityState): Unit = {
    errorScreen.message = message
    errorScreen.fatalityState = fatalityState
    errorScreen.lastScreen = currentScreen

    activeScreen(errorScreen)
  }

  def activateOtherGroup(message: String): Unit = {
    otherGroupScreen.message = message
    otherGroupScreen.lastScreen = currentScreen

    activeScreen(otherGroupScreen)
  }

  def deactivateError(): Unit = activeScreen(errorScreen.lastScreen)

  def deactivateOtherGroupScreen(): Unit = activeScreen(otherGroupScreen.lastScreen)

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

  class InputScreen extends BorderPane with FXMLLoadable with InputFunctionScreen[AbstractResultDTO] {
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
      case wrongLength@WrongLength(barcode) => showEvent(WrongLengthEvent(wrongLength))

      // the barcode has the wrong checksum
      case wrongChecksum@WrongChecksum(barcode) => showEvent(WrongChecksumEvent(wrongChecksum))

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

      case other@_ => Console.err.println(s"Found a strange event: $other")
    }

    def showResult(result: AbstractResultDTO): Unit = result match {
      case IncorrectBarcodeDTO(text) => {
        activateError(s"""Der Barcode "$text" ist nicht valide.""", NonFatal)
      }

      case LendingEntityNotExistsDTO(barcode) => {
        activateError(s"""Der Barcode "$barcode" ist für die Funktion entweder ungültig oder nicht vorhanden.""", NonFatal)
      }

      case LendingEntityInUseDTO(GameDTO(barcode, title, author, publisher, _, _, _), NotInUseDTO()) => {
        activateError(s"""Das Spiel "$barcode" mit dem Titel $title wurde nicht ausgeliehen!""", NonFatalInputReset)
      }

      case ReturnGameSuccessDTO(game@GameDTO(barcode, title, author, publisher, _, _, _)) => {
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
    protected var gamesTableView: TableView[GameDTO] = _

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