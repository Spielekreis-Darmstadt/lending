package info.armado.ausleihe.client.controller.javafx

import info.armado.ausleihe.client.transport.dataobjects.entities.GameDTO
import javafx.scene.control.SkinBase
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.TableColumn._
import scalafx.scene.control.{TableColumn, TableView}

class GameTableViewSkin(gameTableView: GameTableView) extends SkinBase[GameTableView](gameTableView) {
  val tableView: TableView[GameDTO] = new TableView[GameDTO](games) {
    columns ++= List(
      new TableColumn[GameDTO, String] {
        text = "Barcode"
        cellValueFactory = { value => new StringProperty(value.value.barcode) }
        prefWidth = 110
      },
      new TableColumn[GameDTO, String] {
        text = "Titel"
        cellValueFactory = { value => new StringProperty(value.value.title) }
        prefWidth = 400
      },
      new TableColumn[GameDTO, String] {
        text = "Autor"
        cellValueFactory = { value => new StringProperty(value.value.author) }
        prefWidth = 180
      },
      new TableColumn[GameDTO, String] {
        text = "Verlag"
        cellValueFactory = { value => new StringProperty(value.value.publisher) }
        prefWidth = 180
      }
    )
  }

  // automatically scroll to the last element in the list
  games.onChange(games.lastOption.foreach(tableView.scrollTo(_)))

  getChildren.addAll(tableView)

  def games: ObservableBuffer[GameDTO] = getSkinnable.games
}
