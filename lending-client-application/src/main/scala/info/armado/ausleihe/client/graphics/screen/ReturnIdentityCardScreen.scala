package info.armado.ausleihe.client.graphics.screen

import info.armado.ausleihe.client.connection.RestServerConnection
import info.armado.ausleihe.client.graphics.screen.FatalityState.{NonFatal, NonFatalInputReset, Reset}
import info.armado.ausleihe.client.graphics.screen.util.{FXMLLoadable, FocusRequestable}
import info.armado.ausleihe.client.model._
import info.armado.ausleihe.client.transport.dataobjects.entities._
import info.armado.ausleihe.client.transport.dataobjects.inuse._
import info.armado.ausleihe.client.transport.results._
import javafx.beans.binding.Bindings
import javafx.beans.property.{SimpleBooleanProperty, SimpleObjectProperty}
import javafx.fxml.FXML
import javafx.scene.control.{Label, TextField}
import javafx.scene.layout._
import scalafx.Includes._
import scalafx.beans.property.StringProperty.sfxStringProperty2jfx
import scalafx.event.ActionEvent

class ReturnIdentityCardScreen extends StackPane with Screen with FunctionScreen {
  val IdentityCardPrefix: String = Configuration.identityCardPrefix
  val EnvelopePrefix: String = Configuration.envelopePrefix

  val defaultTaskMessage = "Scannen Sie einen Umschlag und einen Ausweis um diesen zurück zunehmen"
  val barcodePromptMessage = "Ausweis-/Umschlagbarcode"

  /**
    * The scanned identity card
    */
  val scannedIdentityCard = new SimpleObjectProperty[IdentityCardDTO](null)

  /**
    * The scanned envelope
    */
  val scannedEnvelope = new SimpleObjectProperty[EnvelopeDTO](null)

  var finished = new SimpleBooleanProperty(false)

  val inputScreen: InputScreen = new InputScreen
  val errorScreen: ErrorScreen = new ErrorScreen

  var currentScreen: Pane with FocusRequestable = _

  loadFXML("javafx/window.fxml")

  @FXML
  def initialize(): Unit = {
    this.children.add(errorScreen)
    this.children.add(inputScreen)

    finished.onChange((observedValue, oldValue, newValue) => inputScreen.styleClass = if (newValue) Array("default-window", "success-screen") else Array("default-window", "input-screen"))

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

  def deactivateError(): Unit = activeScreen(errorScreen.lastScreen)

  def askForFocus(): Unit = Option(currentScreen) match {
    case Some(_: InputScreen) => {
      currentScreen.askForFocus()
    }
    case _ =>
  }

  def totalReset(): Unit = {
    finished.value = false
    scannedEnvelope.setValue(null)
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
    case ErrorScreen(_, _, Some(Reset)) | _ => totalReset()
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
        case _: InputScreen => {
          if (finished.value) {
            scannedIdentityCard.setValue(null)
            scannedEnvelope.setValue(null)

            finished.value = false
          }

          processBarcode(barcodeTextField.text.get)
        }
        case _ =>
      }

      contentPanel = new ContentPanel
      contentFlowPane.children.add(contentPanel)
    }

    def processBarcode(input: String): Unit = (BarcodeTest(input), Option(scannedIdentityCard.getValue), Option(scannedEnvelope.getValue)) match {
      case (wrongLength@WrongLength(barcode), _, _) => showEvent(WrongLengthEvent(wrongLength))

      case (wrongChecksum@WrongChecksum(barcode), _, _) => showEvent(WrongChecksumEvent(wrongChecksum))

      case (Barcode(IdentityCardPrefix, counter, checksum), Some(IdentityCardDTO(`input`, _)), _) => showEvent(ResetEvent)

      case (Barcode(IdentityCardPrefix, counter, checksum), Some(idCardData@IdentityCardDTO(_, _)), _) => showEvent(DifferentRepeatedIdentityCardEvent(idCardData))

      case (Barcode(IdentityCardPrefix, counter, checksum), None, None) => tryResult(RestServerConnection.isBarcodeInUse(input))

      case (Barcode(IdentityCardPrefix, counter, checksum), None, Some(EnvelopeDTO(envelopeBarcode))) => tryResult(RestServerConnection.returnIdentityCard(input, envelopeBarcode))

      case (Barcode(EnvelopePrefix, counter, checksum), _, Some(EnvelopeDTO(`input`))) => showEvent(ResetEvent)

      case (Barcode(EnvelopePrefix, counter, checksum), _, Some(envelopeData@EnvelopeDTO(_))) => showEvent(DifferentRepeatedEnvelopeEvent(envelopeData))

      case (Barcode(EnvelopePrefix, counter, checksum), None, None) => tryResult(RestServerConnection.isBarcodeInUse(input))

      case (Barcode(EnvelopePrefix, counter, checksum), Some(IdentityCardDTO(identityCardBarcode, _)), None) => tryResult(RestServerConnection.returnIdentityCard(identityCardBarcode, input))

      case _ => {
        activateError(s"""Der Barcode $input ist für die Funktion ungültig bzw. nicht vorhanden.""", Reset)
      }
    }

    def showEvent(event: Event): Unit = event match {
      case WrongLengthEvent(_) => {
        activateError("Der Barcode hat die falsche Länge.", NonFatal)
      }

      case WrongChecksumEvent(_) => {
        activateError("Die Prüfsumme des Barcodes ist nicht korrekt!", NonFatal)
      }

      case DifferentRepeatedIdentityCardEvent(IdentityCardDTO(idCardBarcode, _)) => {
        activateError(s"Es wurde bereits ein Ausweis gescannt: $idCardBarcode.", Reset)
      }

      case DifferentRepeatedEnvelopeEvent(EnvelopeDTO(envelopeBarcode)) => {
        activateError(s"Es wurde bereits ein Umschlag $envelopeBarcode gescannt", Reset)
      }

      case ResetEvent => totalReset()

      case other@_ => Console.err.println(s"Found a strange event: $other")
    }

    def showResult(result: AbstractResultDTO): Unit = result match {
      case IncorrectBarcodeDTO(text) => {
        activateError(s"""Der Barcode "$text" ist nicht valide.""", NonFatal)
      }

      case LendingEntityNotExistsDTO(barcode) => {
        activateError(s"""Der Barcode "$barcode" ist für die Funktion entweder ungültig oder nicht vorhanden.""", NonFatal)
      }

      case LendingEntityInUseDTO(EnvelopeDTO(barcode), NotInUseDTO()) => {
        activateError(s"""Der Umschlag "$barcode" wird nicht benutzt und kann somit nicht zurückgegeben werden!""", Reset)
      }

      case LendingEntityInUseDTO(IdentityCardDTO(barcode, None), NotInUseDTO()) => {
        activateError(s"""Der Ausweis "$barcode" ist zur Zeit nicht ausgegeben.""", Reset)
      }

      case LendingEntityInUseDTO(envelopeData@EnvelopeDTO(barcode), EnvelopeInUseDTO(IdentityCardDTO(identityCardBarcode, _), Array())) => {
        scannedEnvelope.value = envelopeData

        taskLabel.text = s"""Scannen sie den Ausweis "$identityCardBarcode" um die Rückgabe abzuschließend."""
        barcodeTextField.text = ""
      }

      case LendingEntityInUseDTO(identityCardData@IdentityCardDTO(barcode, _), IdentityCardInUseDTO(EnvelopeDTO(envelopeBarcode), Array())) => {
        scannedIdentityCard.value = identityCardData

        taskLabel.text = s"""Scannen sie den Umschlag "$envelopeBarcode" um die Rückgabe abzuschließend."""
        barcodeTextField.text = ""
      }

      case LendingEntityInUseDTO(IdentityCardDTO(barcode, None), IdentityCardInUseDTO(EnvelopeDTO(_), games@Array(_, _*))) => {
        activateError((s"""Ausweis "$barcode" hat bereits ein Spiel ausgeliehen:""" :: games.map { game => s"""Spiel "${game.title}" (${game.barcode})""" }.toList).mkString("\n"), Reset)
      }

      case LendingEntityInUseDTO(EnvelopeDTO(_), EnvelopeInUseDTO(IdentityCardDTO(barcode, None), games@Array(_, _*))) => {
        activateError((s"""Ausweis "$barcode" hat bereits ein Spiel ausgeliehen:""" :: games.map { game => s"""Spiel "${game.title}" (${game.barcode})""" }.toList).mkString("\n"), Reset)
      }

      case IdentityCardHasIssuedGamesDTO(IdentityCardDTO(barcode, None), games) => {
        activateError((s"""Ausweis "$barcode" hat bereits ein Spiel ausgeliehen:""" :: games.map { game => s"""Spiel "${game.title}" (${game.barcode})""" }.toList).mkString("\n"), Reset)
      }

      case LendingEntityInUseDTO(IdentityCardDTO(barcode, Some(owner)), IdentityCardInUseDTO(EnvelopeDTO(_), games@Array(_, _*))) => {
        activateError((s"""Ausweis "$barcode" ($owner) hat bereits ein Spiel ausgeliehen:""" :: games.map { game => s"""Spiel "${game.title}" (${game.barcode})""" }.toList).mkString("\n"), Reset)
      }

      case LendingEntityInUseDTO(EnvelopeDTO(_), EnvelopeInUseDTO(IdentityCardDTO(barcode, Some(owner)), games@Array(_, _*))) => {
        activateError((s"""Ausweis "$barcode" ($owner) hat bereits ein Spiel ausgeliehen:""" :: games.map { game => s"""Spiel "${game.title}" (${game.barcode})""" }.toList).mkString("\n"), Reset)
      }

      case IdentityCardHasIssuedGamesDTO(IdentityCardDTO(barcode, Some(owner)), games) => {
        activateError((s"""Ausweis "$barcode" ($owner) hat bereits ein Spiel ausgeliehen:""" :: games.map { game => s"""Spiel "${game.title}" (${game.barcode})""" }.toList).mkString("\n"), Reset)
      }

      case IdentityCardEnvelopeNotBoundDTO(IdentityCardDTO(idCardBarcode, _), EnvelopeDTO(envelopeBarcode), EnvelopeDTO(trueEnvelopeBarcode)) => {
        activateError(s"""Ausweis "$idCardBarcode" passt nicht zu Umschlag $envelopeBarcode, er gehört zu Umschlag: $trueEnvelopeBarcode.""", Reset)
      }

      case ReturnIdentityCardSuccessDTO(identityCard@IdentityCardDTO(idCardBarcode, _), envelope@EnvelopeDTO(envelopeBarcode)) => {
        scannedIdentityCard.value = identityCard
        scannedEnvelope.value = envelope
        finished.value = true

        taskLabel.text = "Scannen Sie einen Umschlag und einen Ausweis um diesen zurück zunehmen"
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
    @FXML protected var identityCardBarcodeLabel: Label = _

    @FXML protected var envelopeBarcodeLabel: Label = _

    this.loadFXML("javafx/identity-card-envelope-information.fxml")

    @FXML
    def initialize(): Unit = {
      this.visibleProperty().bind(Bindings.or(scannedIdentityCard.isNotNull, scannedEnvelope.isNotNull))

      scannedIdentityCard.onChange((observableValue, oldValue, newValue) => Option(newValue) match {
        case None => {
          identityCardBarcodeLabel.text = "Kein Ausweis gescannt."
          identityCardBarcodeLabel.style = "-fx-text-fill: red;"
        }

        case Some(IdentityCardDTO(barcode, None)) => {
          identityCardBarcodeLabel.text = barcode
          identityCardBarcodeLabel.style = ""
        }

        case Some(IdentityCardDTO(barcode, Some(owner))) => {
          identityCardBarcodeLabel.text = s"$barcode ($owner)"
          identityCardBarcodeLabel.style = ""
        }
      })

      scannedEnvelope.onChange((observableValue, oldValue, newValue) => Option(newValue) match {
        case None => {
          envelopeBarcodeLabel.text = "Kein Umschlag gescannt."
          envelopeBarcodeLabel.style = "-fx-text-fill: red;"
        }

        case Some(envelope) => {
          envelopeBarcodeLabel.text = envelope.barcode
          envelopeBarcodeLabel.style = ""
        }
      })
    }
  }

}