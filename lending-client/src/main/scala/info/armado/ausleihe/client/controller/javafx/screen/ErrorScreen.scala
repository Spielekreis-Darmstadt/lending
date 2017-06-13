package info.armado.ausleihe.client.controller.javafx.screen

import javafx.scene.layout.Pane

import javafx.fxml.FXML
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import javafx.scene.control.Label
import scalafx.Includes._

sealed trait FatalityState {
  def todoMessage: String
}
object FatalityState {
  case object NonFatal extends FatalityState {
    def todoMessage: String = "Klicken sie ESC um den Bildschirm zu verlassen und zur aktuellen Eingabe zurückzukehren"
  }
  case object NonFatalInputReset extends FatalityState {
    def todoMessage: String = "Klicken sie ESC um den Bildschirm zu verlassen und zum Eingabeprozess zurückzukehren"
  }
  case object Reset extends FatalityState {
    def todoMessage: String = "Klicken sie ESC um den Bildschirm zu verlassen und den Eingabeprozess zurückzusetzen"
  }
}

object ErrorScreen {
  def unapply(errorScreen: ErrorScreen): Option[(String, String, Option[FatalityState])] = Some((errorScreen.message.value, errorScreen.todoLabel.text.value, Option(errorScreen.fatalityState)))
}
class ErrorScreen extends FlowPane with Resetable with FocusRequestable with FXMLLoadable {
  @FXML
  protected var contentVBox: VBox = _

  @FXML
  protected var messageLabel: Label = _

  @FXML
  protected var todoLabel: Label = _

  var lastScreen: Pane with FocusRequestable = _

  private var _fatalityState: FatalityState = _

  loadFXML("javafx/error.fxml")

  @FXML
  def initialize(): Unit = {
    contentVBox.styleClass = Array("inner-event-panel", "error")
    contentVBox.prefWidthProperty().bind(this.widthProperty().multiply(0.8))
    contentVBox.prefHeightProperty().bind(this.heightProperty().multiply(0.8))
  }

  def message_=(text: String) = messageLabel.text = text
  def message = messageLabel.text

  def fatalityState_=(fatalityState: FatalityState) = {
    _fatalityState = fatalityState
    todoLabel.text = fatalityState.todoMessage
  }

  def fatalityState = _fatalityState

  def askForFocus(): Unit = {
  }

  def reset(): Unit = {
  }
}