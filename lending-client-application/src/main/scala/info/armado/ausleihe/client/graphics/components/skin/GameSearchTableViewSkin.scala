package info.armado.ausleihe.client.graphics.components.skin

import java.time.Duration

import info.armado.ausleihe.client.graphics.components.controller.GameSearchTableView
import info.armado.ausleihe.client.transport.dataobjects.LendGameStatusDTO
import javafx.collections.transformation.SortedList
import javafx.scene.control.SkinBase
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.TableColumn._
import scalafx.scene.control.{TableColumn, TableRow, TableView}

class GameSearchTableViewSkin(gameSearchTableView: GameSearchTableView) extends SkinBase[GameSearchTableView](gameSearchTableView) {
  val barcodeColumn: TableColumn[LendGameStatusDTO, String] = new TableColumn[LendGameStatusDTO, String] {
    text = "Barcode"
    cellValueFactory = { value => new StringProperty(value.value.game.barcode) }
    prefWidth = 110
    editable = false
  }

  val titleColumn: TableColumn[LendGameStatusDTO, String] = new TableColumn[LendGameStatusDTO, String] {
    text = "Titel"
    cellValueFactory = { value => new StringProperty(value.value.game.title) }
    prefWidth = 500
    editable = false
  }

  val authorColumn: TableColumn[LendGameStatusDTO, String] = new TableColumn[LendGameStatusDTO, String] {
    text = "Autor"
    cellValueFactory = { value => new StringProperty(value.value.game.author) }
    prefWidth = 200
    editable = false
  }

  val publisherColumn: TableColumn[LendGameStatusDTO, String] = new TableColumn[LendGameStatusDTO, String] {
    text = "Verlag"
    cellValueFactory = { value => new StringProperty(value.value.game.publisher) }
    prefWidth = 200
    editable = false
  }

  val minimumAgeColumn: TableColumn[LendGameStatusDTO, String] = new TableColumn[LendGameStatusDTO, String] {
    text = "Mindestalter"
    cellValueFactory = { value => new StringProperty(value.value.game.mininumAge) }
    prefWidth = 75
    editable = false
  }

  val playerCountColumn: TableColumn[LendGameStatusDTO, String] = new TableColumn[LendGameStatusDTO, String] {
    text = "Spielerzahl"
    cellValueFactory = { value => new StringProperty(value.value.game.playerCount) }
    prefWidth = 75
    editable = false
  }

  val gameDurationColumn: TableColumn[LendGameStatusDTO, String] = new TableColumn[LendGameStatusDTO, String] {
    text = "Spieldauer"
    cellValueFactory = { value => new StringProperty(value.value.game.gameDuration) }
    prefWidth = 100
    editable = false
  }

  val lendColumn: TableColumn[LendGameStatusDTO, String] = new TableColumn[LendGameStatusDTO, String] {
    text = "Verliehen"
    cellValueFactory = {
      value => new StringProperty(if (value.value.lend) formatDuration(value.value.lendDuration) else "Nicht Verliehen")
    }
    prefWidth = 200
    editable = false
    sortType = SortType.Descending
  }

  val ownerColumn: TableColumn[LendGameStatusDTO, String] = new TableColumn[LendGameStatusDTO, String] {
    text = "An"
    cellValueFactory = {
      value => new StringProperty(value.value.owner)
    }
    prefWidth = 200
    editable = false
    sortType = SortType.Ascending
  }

  val tableView: TableView[LendGameStatusDTO] = new TableView[LendGameStatusDTO]() {
    columns ++= List(barcodeColumn, titleColumn, authorColumn, publisherColumn, minimumAgeColumn, playerCountColumn, gameDurationColumn, lendColumn, ownerColumn)
    sortOrder ++= List(lendColumn, ownerColumn, titleColumn)
    rowFactory = table => new TableRow[LendGameStatusDTO] {
      item.onChange((observable, oldLendGameStatus, newLendGameStatus) =>
        if (Option(newLendGameStatus).exists(_.lend)) {
          this.style = "-fx-background-color: red;"
        } else {
          this.style = ""
        })
    }
  }

  // ensure that the games are sorted
  tableView.items = sortedItems

  getChildren.addAll(tableView)

  def lendGameStatuses: ObservableBuffer[LendGameStatusDTO] = getSkinnable.lendGameStatuses

  def sortedItems: ObservableBuffer[LendGameStatusDTO] = {
    val sortedLendGameStatuses: SortedList[LendGameStatusDTO] =
      new SortedList(lendGameStatuses)

    // bind the sorting settings from the table to the sorted list
    sortedLendGameStatuses.comparatorProperty().bind(tableView.comparator)

    new ObservableBuffer[LendGameStatusDTO](sortedLendGameStatuses)
  }

  def formatDuration(duration: Duration): String = {
    val seconds = duration.getSeconds
    val absSeconds = Math.abs(seconds)
    val positive = f"${absSeconds / 3600}%d:${(absSeconds % 3600) / 60}%02d"
    if (seconds < 0) "-" + positive else positive
  }
}
