package info.armado.ausleihe.client.graphics.components.controller

import info.armado.ausleihe.client.graphics.components.skin.GameTableViewSkin
import info.armado.ausleihe.client.transport.dataobjects.entities.GameDTO
import javafx.scene.control.{Control, Skin}
import scalafx.collections.ObservableBuffer

class GameTableView(val games: ObservableBuffer[GameDTO]) extends Control {
  def this() = this(ObservableBuffer[GameDTO]())

  override def createDefaultSkin(): Skin[_] = new GameTableViewSkin(this)
}
