package info.armado.ausleihe.client.graphics.components.controller

import info.armado.ausleihe.client.graphics.components.skin.OrganizerPanelSkin
import info.armado.ausleihe.client.model.Configuration
import javafx.scene.control.{Control, Skin}
import scalafx.beans.property.StringProperty

class OrganizerPanel(val organizer: StringProperty) extends Control {
  def this() = this(new StringProperty(Configuration.operator))

  override def createDefaultSkin(): Skin[_] = new OrganizerPanelSkin(this)
}
