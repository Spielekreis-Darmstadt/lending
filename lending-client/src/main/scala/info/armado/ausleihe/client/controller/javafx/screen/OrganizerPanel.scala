package info.armado.ausleihe.client.controller.javafx.screen

import javafx.scene.layout.HBox
import javafx.scene.control.Label
import javafx.fxml.FXML
import scalafx.Includes._
import info.armado.ausleihe.client.model.Configuration

class OrganizerPanel extends HBox with FXMLLoadable {
  @FXML
  var organisatorLabel: Label = _
  
  loadFXML("javafx/organizer.fxml")
  organisatorLabel.text = Configuration.operator
}