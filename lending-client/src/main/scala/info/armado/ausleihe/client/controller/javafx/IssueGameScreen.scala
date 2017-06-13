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
import info.armado.ausleihe.remote.dataobjects.entities.EnvelopeData
import info.armado.ausleihe.remote.dataobjects.entities.GameData
import info.armado.ausleihe.remote.dataobjects.entities.IdentityCardData
import info.armado.ausleihe.remote.dataobjects.inuse.GameInUse
import info.armado.ausleihe.remote.dataobjects.inuse.IdentityCardInUse
import info.armado.ausleihe.remote.dataobjects.inuse.NotInUse
import info.armado.ausleihe.remote.results.AbstractResult
import info.armado.ausleihe.remote.results.IdentityCardHasIssuedGames
import info.armado.ausleihe.remote.results.IncorrectBarcode
import info.armado.ausleihe.remote.results.IssueGamesSuccess
import info.armado.ausleihe.remote.results.LendingEntityInUse
import info.armado.ausleihe.remote.results.LendingEntityNotExists
import javafx.beans.NamedArg
import javafx.beans.binding.Bindings
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
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
import scalafx.Includes.jfxBooleanProperty2sfx
import scalafx.Includes.jfxBorderPane2sfx
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

class IssueGameScreen(@NamedArg("limited") limited: Boolean) extends StackPane with Screen with FunctionScreen {
  val GamePrefix = Configuration.gamePrefix
  val OtherGroupPrefix = Configuration.otherGamePrefix
  val IdentityCardPrefix = Configuration.identityCardPrefix

  val scannedGames: ObservableBuffer[GameData] = new ObservableBuffer[GameData]()
  val scannedIdentityCard: ObjectProperty[IdentityCardData] = new SimpleObjectProperty[IdentityCardData](null)

  var finished = new SimpleBooleanProperty(false)

  val defaultTaskMessage = "Scannen Sie ein Spiel und einen Ausweis um das Spiel zu verleihen!"
  val barcodePromptMessage = "Spiele-/Ausweisbarcode"

  val inputScreen: InputScreen = new InputScreen
  val errorScreen: ErrorScreen = new ErrorScreen
  val otherGroupScreen: OtherGroupScreen = new OtherGroupScreen

  var currentScreen: Pane with FocusRequestable = _

  def gameBarcodes: Array[String] = scannedGames.map { _.barcode }.toArray

  def containsBarcode(input: String): Boolean = scannedGames.exists { _.barcode == input }

  loadFXML("javafx/window.fxml")

  @FXML
  def initialize(): Unit = {
    this.children.add(errorScreen)
    this.children.add(otherGroupScreen)
    this.children.add(inputScreen)

    finished.onChange((observedValue, oldValue, newValue) => inputScreen.styleClass = if (newValue) Array("default-window", "success-screen") else Array("default-window", "input-screen"))

    activeScreen(inputScreen)
  }

  def lastGameContainsBarcode(input: String): Boolean = scannedGames.lastOption.exists { _.barcode == input }

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
    finished.value = false
    scannedGames.clear()
    scannedIdentityCard.setValue(null)

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
        case _: InputScreen => {
          if (finished.value) {
            scannedIdentityCard.setValue(null)
            scannedGames.clear()

            finished.value = false
          }

          processBarcode(barcodeTextField.text.get)
        }
        case _ =>
      }

      contentPanel = new ContentPanel
      contentFlowPane.children.add(contentPanel)
    }

    def askForFocus(): Unit = barcodeTextField.requestFocus()

    def reset(): Unit = {
      taskLabel.text = defaultTaskMessage
      barcodeTextField.text = ""
    }

    def processBarcode(input: String): Unit = (BarcodeTest(input), Option(scannedIdentityCard.getValue), scannedGames) match {
      // the scanned barcode has the wrong length
      case (wrongLength @ WrongLength(barcode), _, _) => showEvent(WrongLengthEvent(wrongLength))

      // the scanned barcode has an invalid checksum
      case (wrongChecksum @ WrongChecksum(barcode), _, _) => showEvent(WrongChecksumEvent(wrongChecksum))

      // the scanned game barcode is equal to the last scanned game -> reset
      case (Barcode(GamePrefix, counter, checksum), None, _ :+ GameData(`input`, _, _, _, _, _, _)) => showEvent(ResetEvent)

      // the scanned game barcode was scanned before
      case (Barcode(GamePrefix, counter, checksum), None, ObservableBuffer(_, _*)) if containsBarcode(input) => showEvent(RepeatedGameScanEvent)

      // a game was scanned while no identity card is known as of yet -> add game to the list of scanned games 
      case (Barcode(GamePrefix, counter, checksum), None, ObservableBuffer(_*)) => tryResult(RestServerConnection.isBarcodeInUse(input))

      // a game was scanned while an identity card was scanned before -> finish issuing process
      case (Barcode(GamePrefix, counter, checksum), Some(IdentityCardData(identityCardBarcode, _)), ObservableBuffer()) => tryResult(RestServerConnection.lendGames(identityCardBarcode, Array(input), limited))

      // the scanned identity card was scanned before -> reset
      case (Barcode(IdentityCardPrefix, counter, checksum), Some(IdentityCardData(`input`, _)), ObservableBuffer()) => showEvent(ResetEvent)

      // the scanned identity card is different to the earlier scanned one
      case (Barcode(IdentityCardPrefix, counter, checksum), Some(identityCard @ IdentityCardData(_, _)), ObservableBuffer()) => showEvent(DifferentRepeatedIdentityCardEvent(identityCard))

      // an identity card is scanned while neither an identity card nor a game was scanned before
      case (Barcode(IdentityCardPrefix, counter, checksum), None, ObservableBuffer()) => tryResult(RestServerConnection.isBarcodeInUse(input))

      // a list of at least one game was scanned before and an identity card is scanned now -> finish issuing process
      case (Barcode(IdentityCardPrefix, counter, checksum), None, ObservableBuffer(_, _*)) => tryResult(RestServerConnection.lendGames(input, gameBarcodes, limited))

      // the scanned barcode could belong to a game that belongs to the other group
      case (Barcode(OtherGroupPrefix, counter, checksum), _, _) => showEvent(OtherGroupGameEvent(input))

      // something totally different was scanned that couldn't be processed before
      case _ => {
        activateError(s"""Der Barcode $input ist für die Funktion ungültig bzw. nicht vorhanden.""", Reset)
      }
    }

    def showResult(result: AbstractResult): Unit = result match {
      case IncorrectBarcode(text) => {
        activateError(s"""Der Barcode "$text" ist nicht valide.""", NonFatal)
      }

      case LendingEntityNotExists(barcode) => {
        activateError(s"""Der Barcode "$barcode" ist für die Funktion entweder ungültig oder nicht vorhanden.""", NonFatal)
      }

      case IdentityCardHasIssuedGames(IdentityCardData(barcode, None), games) => {
        activateError(s"""Der Ausweis "$barcode" hat bereits mindestens ein Spiel ausgeliehen.""", Reset)
      }

      case IdentityCardHasIssuedGames(IdentityCardData(barcode, Some(owner)), games) => {
        activateError(s"""Der Ausweis "$barcode" ($owner) hat bereits mindestens ein Spiel ausgeliehen.""", Reset)
      }

      case LendingEntityInUse(IdentityCardData(barcode, None), IdentityCardInUse(EnvelopeData(_), Array(_, _*))) if limited => {
        activateError(s"""Der Ausweis "$barcode" hat bereits mindestens ein Spiel ausgeliehen.""", Reset)
      }

      case LendingEntityInUse(IdentityCardData(barcode, Some(owner)), IdentityCardInUse(EnvelopeData(_), Array(_, _*))) if limited => {
        activateError(s"""Der Ausweis "$barcode" ($owner) hat bereits mindestens ein Spiel ausgeliehen.""", Reset)
      }

      case LendingEntityInUse(GameData(barcode, title, _, _, _, _, _), GameInUse(IdentityCardData(_, _), EnvelopeData(_))) => {
        activateError(s"""Das Spiel "$title" ($barcode) ist bereits verliehen.""", Reset)
      }

      case LendingEntityInUse(IdentityCardData(barcode, None), NotInUse()) => {
        activateError(s"""Der Ausweis "$barcode" ist zur Zeit nicht ausgegeben""", Reset)
      }

      case LendingEntityInUse(game @ GameData(_, _, _, _, _, _, _), NotInUse()) => {
        scannedGames += game

        taskLabel.text = "Scannen Sie nun einen Ausweis oder ein weiteres Spiel."
        barcodeTextField.text = ""
      }

      case LendingEntityInUse(identityCard @ IdentityCardData(_, _), IdentityCardInUse(EnvelopeData(_), Array())) => {
        scannedIdentityCard.value = identityCard

        taskLabel.text = "Scannen Sie jetzt ein Spiel."
        barcodeTextField.text = ""
      }

      case LendingEntityInUse(identityCard @ IdentityCardData(_, _), IdentityCardInUse(EnvelopeData(_), _)) if !limited => {
        scannedIdentityCard.value = identityCard

        taskLabel.text = "Scannen Sie jetzt ein Spiel."
        barcodeTextField.text = ""
      }

      case IssueGamesSuccess(identityCard @ IdentityCardData(_, _), games) => {
        scannedIdentityCard.value = identityCard
        scannedGames.setAll(games: _*)
        finished.value = true

        taskLabel.text = "Scannen Sie ein Spiel und einen Ausweis um das Spiel zu verleihen!"
        barcodeTextField.text = ""
      }
    }

    def showEvent(event: Event): Unit = event match {
      case WrongLengthEvent(_) => {
        activateError("Der Barcode hat die falsche Länge.", NonFatal)
      }

      case WrongChecksumEvent(_) => {
        activateError("Die Prüfsumme des Barcodes ist nicht korrekt!", NonFatal)
      }

      case RepeatedGameScanEvent => {
        activateError("Das gescannte Spiel wurde bereits zuvor gescannt.", Reset)
      }

      case DifferentRepeatedIdentityCardEvent(IdentityCardData(barcode, _)) => {
        activateError(s"Es wurde bereits ein Ausweis gescannt: $barcode", Reset)
      }

      case OtherGroupGameEvent(barcode) => {
        activateOtherGroup(s"Das gescannte Spiel $barcode gehört zur anderen Gruppe, bitte Ausleiher dort hin verweisen!")
      }

      case ResetEvent => totalReset()

      case other @ _ => Console.err.println(s"Found a strange event: $other")
    }
  }

  class ContentPanel extends VBox with FXMLLoadable {
    @FXML
    protected var identityCardBarcodeLabel: Label = _

    @FXML
    protected var gamesTableView: TableView[GameData] = _

    this.loadFXML("javafx/issue-games-information.fxml")

    @FXML
    def initialize(): Unit = {
      this.visibleProperty().bind(Bindings.or(scannedIdentityCard.isNotNull(), Bindings.isNotEmpty(scannedGames)))

      scannedIdentityCard.onChange((observableValue, oldValue, newValue) => Option(newValue) match {
        case None => {
          identityCardBarcodeLabel.text = "Kein Ausweis gescannt."
          identityCardBarcodeLabel.style = "-fx-text-fill: red;"
        }
        case Some(IdentityCardData(barcode, None)) => {
          identityCardBarcodeLabel.text = barcode
          identityCardBarcodeLabel.style = ""
        }
        case Some(IdentityCardData(barcode, Some(owner))) => {
          identityCardBarcodeLabel.text = s"$barcode ($owner)"
          identityCardBarcodeLabel.style = ""
        }
      });

      gamesTableView.items = scannedGames
      gamesTableView.items.onChange(scannedGames.lastOption match {
        case Some(lastGame) => gamesTableView.scrollTo(lastGame)
        case None =>
      })
    }
  }

  object :+ {
    def unapply[A](l: ObservableBuffer[A]): Option[(ObservableBuffer[A], A)] = {
      if (l.isEmpty)
        None
      else
        Some(l.init, l.last)
    }
  }
}