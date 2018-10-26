package info.armado.ausleihe.client.controller.javafx

import java.time.Duration

import info.armado.ausleihe.client.connection.RestServerConnection
import info.armado.ausleihe.client.controller.javafx.screen.FatalityState._
import info.armado.ausleihe.client.controller.javafx.screen._
import info.armado.ausleihe.client.transport.dataobjects.LendGameStatusDTO
import info.armado.ausleihe.client.transport.dataobjects.information.GameInformationDTO
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.layout._
import scalafx.Includes._
import scalafx.beans.property.ReadOnlyStringWrapper
import scalafx.event.ActionEvent

class SearchGamesScreen extends StackPane with Screen with FunctionScreen {
  val searchResult = new SimpleObjectProperty[GameInformationDTO](null)

  val inputScreen = new InputScreen
  val outputScreen = new OutputScreen
  val errorScreen = new ErrorScreen

  var currentScreen: Pane with FocusRequestable = inputScreen

  loadFXML("javafx/window.fxml")

  @FXML
  def initialize(): Unit = {
    this.children.add(errorScreen)
    this.children.add(outputScreen)
    this.children.add(inputScreen)

    activeScreen(inputScreen)
  }

  def activeScreen(screen: Pane with FocusRequestable): Unit = {
    screen.toFront()
    screen.askForFocus()
    currentScreen = screen
  }

  def activateError(message: String, fatalityState: FatalityState): Unit = {
    errorScreen.message = message
    errorScreen.fatalityState = fatalityState
    errorScreen.lastScreen = currentScreen

    activeScreen(errorScreen)
  }

  def deactivateError(): Unit = {
    activeScreen(errorScreen.lastScreen)
  }

  def askForFocus(): Unit = Option(currentScreen) match {
    case Some(_: InputScreen) => {
      currentScreen.askForFocus()
    }
    case _ =>
  }

  def totalReset(): Unit = {
    inputScreen.reset()
    activeScreen(inputScreen)
  }

  def reset(): Unit = currentScreen match {
    case ErrorScreen(_, _, Some(NonFatal)) => deactivateError()
    case ErrorScreen(_, _, Some(NonFatalInputReset)) | ErrorScreen(_, _, Some(Reset)) | _ => totalReset()
  }

  class InputScreen extends BorderPane with FXMLLoadable with InputFunctionScreen[GameInformationDTO] {
    @FXML protected var taskLabel: Label = _

    @FXML protected var barcodeTextField: TextField = _

    @FXML var contentFlowPane: FlowPane = _

    var contentPanel: ContentPanel = _

    loadFXML("javafx/borrow.fxml")

    @FXML
    def initialize(): Unit = {
      barcodeTextField.onAction = (event: ActionEvent) => currentScreen match {
        case _: InputScreen => processInput()
        case _ =>
      }

      contentPanel = new ContentPanel
      contentFlowPane.children.add(contentPanel)

      reset()
    }

    def processInput(): Unit = (barcodeTextField.text.get, contentPanel.toTuple) match {
      case (searchTerm, (_, _, _, _, _, _)) if !searchTerm.isEmpty && searchTerm.length < 3 => {
        activateError("Der angegebene Suchbegriff ist kleiner als 3 Zeichen lang.", NonFatal)
      }

      case (searchTerm, (title, _, _, _, _, _)) if !title.isEmpty && title.length < 3 => {
        activateError("Der angegebene Titel ist kleiner als 3 Zeichen lang.", NonFatal)
      }

      case (searchTerm, (_, author, _, _, _, _)) if !author.isEmpty && author.length < 3 => {
        activateError("Der angegebene Author ist kleiner als 3 Zeichen lang.", NonFatal)
      }

      case (searchTerm, (_, _, publisher, _, _, _)) if !publisher.isEmpty && publisher.length < 3 => {
        activateError("Der angegebene Verlag ist kleiner als 3 Zeichen lang.", NonFatal)
      }

      case (searchTerm, (title, author, publisher, minimumAge, playerCount, gameDuration)) => {
        val stringConvert: String => String = input => if (input == null || input.isEmpty) null else input
        val integerConvert: String => Integer = input => if (input == null || input.isEmpty || !isAllDigits(input)) null else input.toInt

        tryResult(RestServerConnection.searchGames(stringConvert(searchTerm), stringConvert(title), stringConvert(author), stringConvert(publisher), integerConvert(minimumAge), integerConvert(playerCount), integerConvert(gameDuration)))
      }
    }

    def isAllDigits(x: String): Boolean = x forall Character.isDigit

    def reset(): Unit = {
      taskLabel.text = "Geben sie bitte den Titel eines Spiels ein, um angezeigt zu bekommen, ob dieses gerade verliehen ist"
      barcodeTextField.promptText = "Suchbegriff"
      barcodeTextField.text = ""
      contentPanel.reset()

      askForFocus()
    }

    def askForFocus(): Unit = barcodeTextField.requestFocus()

    def showResult(result: GameInformationDTO): Unit = {
      searchResult.value = result

      activeScreen(outputScreen)
    }
  }

  class ContentPanel extends VBox with FXMLLoadable with Resetable {
    @FXML var collapsablePanel: TitledPane = _

    @FXML var titleField: TextField = _
    @FXML var authorField: TextField = _
    @FXML var publisherField: TextField = _
    @FXML var minimumAgeField: TextField = _
    @FXML var playerCountField: TextField = _
    @FXML var gameDurationField: TextField = _

    loadFXML("javafx/game-search-information.fxml")

    @FXML
    def initialize(): Unit = {
      titleField.onAction = (event: ActionEvent) => inputScreen.processInput()
      authorField.onAction = (event: ActionEvent) => inputScreen.processInput()
      publisherField.onAction = (event: ActionEvent) => inputScreen.processInput()
      minimumAgeField.onAction = (event: ActionEvent) => inputScreen.processInput()
      playerCountField.onAction = (event: ActionEvent) => inputScreen.processInput()
      gameDurationField.onAction = (event: ActionEvent) => inputScreen.processInput()
    }

    def reset(): Unit = {
      collapsablePanel.expanded = false

      titleField.text = ""
      authorField.text = ""
      publisherField.text = ""
      minimumAgeField.text = ""
      playerCountField.text = ""
      gameDurationField.text = ""
    }

    def toTuple: (String, String, String, String, String, String) = (titleField.text.get, authorField.text.get, publisherField.text.get, minimumAgeField.text.get, playerCountField.text.get, gameDurationField.text.get)
  }

  class OutputScreen extends BorderPane with FXMLLoadable with BlankOutputFunctionScreen {
    @FXML var searchTermLabel: Label = _

    @FXML var foundGamesTableView: TableView[LendGameStatusDTO] = _

    @FXML var barcodeColumn: TableColumn[LendGameStatusDTO, String] = _
    @FXML var titleColumn: TableColumn[LendGameStatusDTO, String] = _
    @FXML var authorColumn: TableColumn[LendGameStatusDTO, String] = _
    @FXML var publisherColumn: TableColumn[LendGameStatusDTO, String] = _
    @FXML var minimumAgeColumn: TableColumn[LendGameStatusDTO, String] = _
    @FXML var playerCountColumn: TableColumn[LendGameStatusDTO, String] = _
    @FXML var gameDurationColumn: TableColumn[LendGameStatusDTO, String] = _
    @FXML var lendColumn: TableColumn[LendGameStatusDTO, String] = _

    loadFXML("javafx/game-search-result.fxml")

    @FXML
    def initialize(): Unit = {
      barcodeColumn.cellValueFactory = cellData => ReadOnlyStringWrapper(cellData.value.game.barcode)
      titleColumn.cellValueFactory = cellData => ReadOnlyStringWrapper(cellData.value.game.title)
      authorColumn.cellValueFactory = cellData => ReadOnlyStringWrapper(cellData.value.game.author)
      publisherColumn.cellValueFactory = cellData => ReadOnlyStringWrapper(cellData.value.game.publisher)
      minimumAgeColumn.cellValueFactory = cellData => ReadOnlyStringWrapper(cellData.value.game.mininumAge)
      playerCountColumn.cellValueFactory = cellData => ReadOnlyStringWrapper(cellData.value.game.playerCount)
      gameDurationColumn.cellValueFactory = cellData => ReadOnlyStringWrapper(cellData.value.game.gameDuration)

      lendColumn.cellValueFactory = cellData => cellData.value.lend match {
        case true => ReadOnlyStringWrapper(formatDuration(cellData.value.lendDuration))
        case false => ReadOnlyStringWrapper("Nicht verliehen")
      }

      foundGamesTableView.setRowFactory((table: TableView[LendGameStatusDTO]) => new TableRow[LendGameStatusDTO] {
        override def updateItem(rowValue: LendGameStatusDTO, empty: Boolean): Unit = {
          super.updateItem(rowValue, empty)

          if (Option(rowValue).exists(_.lend)) {
            this.style = "-fx-background-color: red;"
          } else {
            this.style = ""
          }
        }
      })

      searchResult.onChange((observableValue, oldValue, newValue) => Option(newValue) match {
        case Some(gameInformation) => {
          searchTermLabel.text = gameInformation.request.searchTerm
          foundGamesTableView.items.get.setAll(gameInformation.foundGames: _*)
          foundGamesTableView.sort()
        }
        case None =>
      })
    }

    def formatDuration(duration: Duration): String = {
      val seconds = duration.getSeconds
      val absSeconds = Math.abs(seconds)
      val positive = f"${absSeconds / 3600}%d:${(absSeconds % 3600) / 60}%02d"
      if (seconds < 0) "-" + positive else positive
    }

    override def askForFocus(): Unit = foundGamesTableView.requestFocus()
  }

}