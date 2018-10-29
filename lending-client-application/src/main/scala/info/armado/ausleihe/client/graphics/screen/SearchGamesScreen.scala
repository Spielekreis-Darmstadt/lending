package info.armado.ausleihe.client.graphics.screen

import info.armado.ausleihe.client.connection.RestServerConnection
import info.armado.ausleihe.client.graphics.components.controller.GameSearchTableView
import info.armado.ausleihe.client.graphics.screen.FatalityState._
import info.armado.ausleihe.client.graphics.screen.util.{FXMLLoadable, FocusRequestable, Resetable}
import info.armado.ausleihe.client.transport.dataobjects.information.GameInformationDTO
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.scene.control._
import javafx.scene.layout._
import scalafx.Includes._
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

    @FXML var foundGamesTableView: GameSearchTableView = _

    loadFXML("javafx/game-search-result.fxml")

    @FXML
    def initialize(): Unit = {
      searchResult.onChange((observableValue, oldValue, newValue) => Option(newValue) match {
        case Some(gameInformation) => {
          searchTermLabel.text = gameInformation.request.searchTerm
          foundGamesTableView.lendGameStatuses.setAll(gameInformation.foundGames: _*)
        }
        case None =>
      })
    }

    override def askForFocus(): Unit = foundGamesTableView.requestFocus()
  }

}