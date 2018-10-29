package info.armado.ausleihe.client.graphics.components.skin

import info.armado.ausleihe.client.graphics.components.controller.OrganizerPanel
import javafx.scene.control.SkinBase
import scalafx.beans.property.StringProperty
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{ContentDisplay, Label}
import scalafx.scene.layout.HBox
import scalafx.scene.text.TextAlignment

class OrganizerPanelSkin(organizerPanel: OrganizerPanel) extends SkinBase[OrganizerPanel](organizerPanel) {
  val box: HBox = new HBox() {
    alignment = Pos.BottomRight
    children = new Label() {
      wrapText = true
      alignment = Pos.Center
      contentDisplay = ContentDisplay.Center
      textAlignment = TextAlignment.Center
      margin = Insets(5)
      text <== organizer
    }
  }

  getChildren.addAll(box)

  def organizer: StringProperty = getSkinnable.organizer
}
