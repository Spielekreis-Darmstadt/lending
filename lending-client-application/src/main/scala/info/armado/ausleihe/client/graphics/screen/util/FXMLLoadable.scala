package info.armado.ausleihe.client.graphics.screen.util

import javafx.fxml.FXMLLoader
import javafx.scene.Node

trait FXMLLoadable {
  def loadFXML(filename: String): Node = {
    val fxmlLoader = new FXMLLoader(classOf[FXMLLoadable].getClassLoader().getResource(filename));

    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    fxmlLoader.load();
  }
}
