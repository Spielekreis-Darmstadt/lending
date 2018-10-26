package info.armado.ausleihe.client.controller.javafx.screen

import javafx.fxml.FXML
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import javafx.scene.control.Label
import scalafx.Includes._
import javafx.scene.layout.Pane

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

  def message_=(text: String) = messageLabel.text = text
  def message = messageLabel.text

  def askForFocus(): Unit = {
  }

  def reset(): Unit = {
  }
}