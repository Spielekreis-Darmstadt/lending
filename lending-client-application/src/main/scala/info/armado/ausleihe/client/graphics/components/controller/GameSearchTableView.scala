package info.armado.ausleihe.client.graphics.components.controller

import info.armado.ausleihe.client.graphics.components.skin.GameSearchTableViewSkin
import info.armado.ausleihe.client.transport.dataobjects.LendGameStatusDTO
import javafx.scene.control.{Control, Skin}
import scalafx.collections.ObservableBuffer

class GameSearchTableView(val lendGameStatuses: ObservableBuffer[LendGameStatusDTO]) extends Control {
  def this() = this(ObservableBuffer[LendGameStatusDTO]())

  override def createDefaultSkin(): Skin[_] = new GameSearchTableViewSkin(this)
}
