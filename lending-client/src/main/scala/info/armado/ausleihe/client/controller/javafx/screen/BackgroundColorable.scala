package info.armado.ausleihe.client.controller.javafx.screen

import javafx.scene.layout.Pane
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Background
import javafx.geometry.Insets
import javafx.scene.paint.Color
import javafx.scene.layout.VBox

trait BackgroundColorable {
  self: Pane =>
    def background_=(color: Color): Unit = setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)))
    def background = getBackground

}