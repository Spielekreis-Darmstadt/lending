package info.armado.ausleihe.client.graphics.screen

import info.armado.ausleihe.client.graphics.screen.util.{FXMLLoadable, FocusRequestable, Resetable}
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.{FlowPane, Pane, VBox}
import scalafx.Includes._
import scalafx.beans.property.StringProperty

object OtherGroupScreen {
  def unapply(otherGroupScreen: OtherGroupScreen): Option[(String, String)] = Some((otherGroupScreen.message.value, otherGroupScreen.todoLabel.text.value))
}

class OtherGroupScreen extends FlowPane with Resetable with FocusRequestable with FXMLLoadable {
  @FXML
  protected var contentVBox: VBox = _

  @FXML
  protected var messageLabel: Label = _

  @FXML
  protected var todoLabel: Label = _

  var lastScreen: Pane with FocusRequestable = _

  loadFXML("javafx/error.fxml")

  @FXML
  def initialize(): Unit = {
    contentVBox.styleClass = Array("inner-event-panel", "other-group")
    contentVBox.prefWidthProperty().bind(this.widthProperty().multiply(0.8))
    contentVBox.prefHeightProperty().bind(this.heightProperty().multiply(0.8))

    todoLabel.text = "Klicken sie ESC um den Bildschirm zu verlassen und zur aktullen Eingabe zurückzukehren"
  }

  def message_=(text: String): Unit = messageLabel.text = text

  def message: StringProperty = messageLabel.text

  def askForFocus(): Unit = {
  }

  def reset(): Unit = {
  }
}