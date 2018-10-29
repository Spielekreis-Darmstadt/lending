package info.armado.ausleihe.client.graphics.screen.util

import javafx.geometry.Insets
import javafx.scene.layout.{Background, BackgroundFill, CornerRadii, Pane}
import javafx.scene.paint.Color

trait BackgroundColorable {
  self: Pane =>
    def background_=(color: Color): Unit = setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)))
    def background: Background = getBackground

}
