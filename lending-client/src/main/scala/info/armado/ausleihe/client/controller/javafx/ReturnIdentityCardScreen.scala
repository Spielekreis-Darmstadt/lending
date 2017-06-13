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
import info.armado.ausleihe.client.model.Configuration
import info.armado.ausleihe.client.model.WrongChecksum
import info.armado.ausleihe.client.model.WrongLength
import info.armado.ausleihe.remote.dataobjects.entities.EnvelopeData
import info.armado.ausleihe.remote.dataobjects.entities.IdentityCardData
import info.armado.ausleihe.remote.dataobjects.inuse.EnvelopeInUse
import info.armado.ausleihe.remote.dataobjects.inuse.IdentityCardInUse
import info.armado.ausleihe.remote.dataobjects.inuse.NotInUse
import info.armado.ausleihe.remote.results.AbstractResult
import info.armado.ausleihe.remote.results.IdentityCardEnvelopeNotBound
import info.armado.ausleihe.remote.results.IdentityCardHasIssuedGames
import info.armado.ausleihe.remote.results.IncorrectBarcode
import info.armado.ausleihe.remote.results.LendingEntityInUse
import info.armado.ausleihe.remote.results.LendingEntityNotExists
import info.armado.ausleihe.remote.results.ReturnIdentityCardSuccess
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.scene.control.Label
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
import scalafx.Includes.jfxTextField2sfx
import scalafx.beans.property.StringProperty.sfxStringProperty2jfx
import scalafx.event.ActionEvent
import info.armado.ausleihe.client.controller.javafx.screen.FunctionScreen
import info.armado.ausleihe.client.controller.javafx.screen.InputScreen

class ReturnIdentityCardScreen extends StackPane with Screen with FunctionScreen {
  val IdentityCardPrefix = Configuration.identityCardPrefix
  val EnvelopePrefix = Configuration.envelopePrefix

  val defaultTaskMessage = "Scannen Sie einen Umschlag und einen Ausweis um diesen zurück zunehmen"
  val barcodePromptMessage = "Ausweis-/Umschlagbarcode"

  /**
   * The scanned identity card
   */
  val scannedIdentityCard = new SimpleObjectProperty[IdentityCardData](null)

  /**
   * The scanned envelope
   */
  val scannedEnvelope = new SimpleObjectProperty[EnvelopeData](null)

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

  def deactivateError() = activeScreen(errorScreen.lastScreen)

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
      case (wrongLength @ WrongLength(barcode), _, _) => showEvent(WrongLengthEvent(wrongLength))

      case (wrongChecksum @ WrongChecksum(barcode), _, _) => showEvent(WrongChecksumEvent(wrongChecksum))

      case (Barcode(IdentityCardPrefix, counter, checksum), Some(IdentityCardData(`input`, _)), _) => showEvent(ResetEvent)

      case (Barcode(IdentityCardPrefix, counter, checksum), Some(idCardData @ IdentityCardData(_, _)), _) => showEvent(DifferentRepeatedIdentityCardEvent(idCardData))

      case (Barcode(IdentityCardPrefix, counter, checksum), None, None) => tryResult(RestServerConnection.isBarcodeInUse(input))

      case (Barcode(IdentityCardPrefix, counter, checksum), None, Some(EnvelopeData(envelopeBarcode))) => tryResult(RestServerConnection.returnIdentityCard(input, envelopeBarcode))

      case (Barcode(EnvelopePrefix, counter, checksum), _, Some(EnvelopeData(`input`))) => showEvent(ResetEvent)

      case (Barcode(EnvelopePrefix, counter, checksum), _, Some(envelopeData @ EnvelopeData(_))) => showEvent(DifferentRepeatedEnvelopeEvent(envelopeData))

      case (Barcode(EnvelopePrefix, counter, checksum), None, None) => tryResult(RestServerConnection.isBarcodeInUse(input))

      case (Barcode(EnvelopePrefix, counter, checksum), Some(IdentityCardData(identityCardBarcode, _)), None) => tryResult(RestServerConnection.returnIdentityCard(identityCardBarcode, input))

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

      case DifferentRepeatedIdentityCardEvent(IdentityCardData(idCardBarcode, _)) => {
        activateError(s"Es wurde bereits ein Ausweis gescannt: $idCardBarcode.", Reset)
      }

      case DifferentRepeatedEnvelopeEvent(EnvelopeData(envelopeBarcode)) => {
        activateError(s"Es wurde bereits ein Umschlag $envelopeBarcode gescannt", Reset)
      }

      case ResetEvent => totalReset()

      case other @ _ => Console.err.println(s"Found a strange event: $other")
    }

    def showResult(result: AbstractResult): Unit = result match {
      case IncorrectBarcode(text) => {
        activateError(s"""Der Barcode "$text" ist nicht valide.""", NonFatal)
      }

      case LendingEntityNotExists(barcode) => {
        activateError(s"""Der Barcode "$barcode" ist für die Funktion entweder ungültig oder nicht vorhanden.""", NonFatal)
      }

      case LendingEntityInUse(EnvelopeData(barcode), NotInUse()) => {
        activateError(s"""Der Umschlag "$barcode" wird nicht benutzt und kann somit nicht zurückgegeben werden!""", Reset)
      }

      case LendingEntityInUse(IdentityCardData(barcode, None), NotInUse()) => {
        activateError(s"""Der Ausweis "$barcode" ist zur Zeit nicht ausgegeben.""", Reset)
      }

      case LendingEntityInUse(envelopeData @ EnvelopeData(barcode), EnvelopeInUse(IdentityCardData(identityCardBarcode, _), Array())) => {
        scannedEnvelope.value = envelopeData

        taskLabel.text = s"""Scannen sie den Ausweis "$identityCardBarcode" um die Rückgabe abzuschließend."""
        barcodeTextField.text = ""
      }

      case LendingEntityInUse(identityCardData @ IdentityCardData(barcode, _), IdentityCardInUse(EnvelopeData(envelopeBarcode), Array())) => {
        scannedIdentityCard.value = identityCardData

        taskLabel.text = s"""Scannen sie den Umschlag "$envelopeBarcode" um die Rückgabe abzuschließend."""
        barcodeTextField.text = ""
      }

      case LendingEntityInUse(IdentityCardData(barcode, None), IdentityCardInUse(EnvelopeData(_), games @ Array(_, _*))) => {
        activateError((s"""Ausweis "$barcode" hat bereits ein Spiel ausgeliehen:""" :: games.map { game => s"""Spiel "${game.title}" (${game.barcode})""" }.toList).mkString("\n"), Reset)
      }

      case LendingEntityInUse(EnvelopeData(_), EnvelopeInUse(IdentityCardData(barcode, None), games @ Array(_, _*))) => {
        activateError((s"""Ausweis "$barcode" hat bereits ein Spiel ausgeliehen:""" :: games.map { game => s"""Spiel "${game.title}" (${game.barcode})""" }.toList).mkString("\n"), Reset)
      }

      case IdentityCardHasIssuedGames(IdentityCardData(barcode, None), games) => {
        activateError((s"""Ausweis "$barcode" hat bereits ein Spiel ausgeliehen:""" :: games.map { game => s"""Spiel "${game.title}" (${game.barcode})""" }.toList).mkString("\n"), Reset)
      }

      case LendingEntityInUse(IdentityCardData(barcode, Some(owner)), IdentityCardInUse(EnvelopeData(_), games @ Array(_, _*))) => {
        activateError((s"""Ausweis "$barcode" ($owner) hat bereits ein Spiel ausgeliehen:""" :: games.map { game => s"""Spiel "${game.title}" (${game.barcode})""" }.toList).mkString("\n"), Reset)
      }

      case LendingEntityInUse(EnvelopeData(_), EnvelopeInUse(IdentityCardData(barcode, Some(owner)), games @ Array(_, _*))) => {
        activateError((s"""Ausweis "$barcode" ($owner) hat bereits ein Spiel ausgeliehen:""" :: games.map { game => s"""Spiel "${game.title}" (${game.barcode})""" }.toList).mkString("\n"), Reset)
      }

      case IdentityCardHasIssuedGames(IdentityCardData(barcode, Some(owner)), games) => {
        activateError((s"""Ausweis "$barcode" ($owner) hat bereits ein Spiel ausgeliehen:""" :: games.map { game => s"""Spiel "${game.title}" (${game.barcode})""" }.toList).mkString("\n"), Reset)
      }

      case IdentityCardEnvelopeNotBound(IdentityCardData(idCardBarcode, _), EnvelopeData(envelopeBarcode), EnvelopeData(trueEnvelopeBarcode)) => {
        activateError(s"""Ausweis "$idCardBarcode" passt nicht zu Umschlag $envelopeBarcode, er gehört zu Umschlag: $trueEnvelopeBarcode.""", Reset)
      }

      case ReturnIdentityCardSuccess(identityCard @ IdentityCardData(idCardBarcode, _), envelope @ EnvelopeData(envelopeBarcode)) => {
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
      this.visibleProperty().bind(Bindings.or(scannedIdentityCard.isNotNull(), scannedEnvelope.isNotNull()))

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

      scannedEnvelope.onChange((observableValue, oldValue, newValue) => Option(newValue) match {
        case None => {
          envelopeBarcodeLabel.text = "Kein Umschlag gescannt."
          envelopeBarcodeLabel.style = "-fx-text-fill: red;"
        }

        case Some(envelope) => {
          envelopeBarcodeLabel.text = envelope.barcode
          envelopeBarcodeLabel.style = ""
        }
      });
    }
  }
}