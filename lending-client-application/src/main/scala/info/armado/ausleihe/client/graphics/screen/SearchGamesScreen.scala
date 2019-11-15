package info.armado.ausleihe.client.graphics.screen

import info.armado.ausleihe.client.model.SearchDetails
import info.armado.ausleihe.client.connection.RestServerConnection
import info.armado.ausleihe.client.graphics.components.controller.GameSearchTableView
import info.armado.ausleihe.client.graphics.screen.FatalityState._
import info.armado.ausleihe.client.graphics.screen.util.{FXMLLoadable, FocusRequestable, Resetable}
import info.armado.ausleihe.client.transport.dataobjects.information.GameInformationDTO

import java.time.Year

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
    case ErrorScreen(_, _, Some(NonFatalInputReset)) | ErrorScreen(_, _, Some(Reset)) | _ =>
      totalReset()
  }

  def isAllDigits(x: String): Boolean = x forall Character.isDigit

  def convertString(input: String): Option[String] = Option(input).filter(_.nonEmpty)

  def convertInteger(input: String): Option[Integer] =
    Option(input)
      .filter(input => input.nonEmpty && isAllDigits(input))
      .map(input => input.toInt)

  class InputScreen
      extends BorderPane
      with FXMLLoadable
      with InputFunctionScreen[GameInformationDTO] {
    @FXML protected var taskLabel: Label = _

    @FXML protected var barcodeTextField: TextField = _

    @FXML var contentFlowPane: FlowPane = _

    var contentPanel: ContentPanel = _

    loadFXML("javafx/borrow.fxml")

    @FXML
    def initialize(): Unit = {
      barcodeTextField.onAction = (event: ActionEvent) =>
        currentScreen match {
          case _: InputScreen => processInput()
          case _              =>
        }

      contentPanel = new ContentPanel
      contentFlowPane.children.add(contentPanel)

      reset()
    }

    def processInput(): Unit =
      (convertString(barcodeTextField.text.get), contentPanel.toDetails) match {
        case (None, SearchDetails(None, None, None, None, None, None, None)) =>
          activateError("Fehlende Eingabe", NonFatal)

        case (Some(searchTerm), _) if searchTerm.length < 3 =>
          activateError("Der angegebene Suchbegriff ist kleiner als 3 Zeichen lang.", NonFatal)

        case (_, SearchDetails(Some(title), _, _, _, _, _, _)) if title.length < 3 =>
          activateError("Der angegebene Titel ist kleiner als 3 Zeichen lang.", NonFatal)

        case (_, SearchDetails(_, Some(author), _, _, _, _, _)) if author.length < 3 =>
          activateError("Der angegebene Author ist kleiner als 3 Zeichen lang.", NonFatal)

        case (_, SearchDetails(_, _, Some(publisher), _, _, _, _)) if publisher.length < 3 =>
          activateError("Der angegebene Verlag ist kleiner als 3 Zeichen lang.", NonFatal)

        case (Some("Neuheiten"), result) =>
          tryResult(
            RestServerConnection
              .searchGames(null, result.copy(releaseYear = Some(Year.now.getValue)))
          )

        case (searchTerm, result) =>
          tryResult(RestServerConnection.searchGames(searchTerm.orNull, result))
      }

    def reset(): Unit = {
      taskLabel.text =
        "Geben sie bitte den Titel eines Spiels ein, um angezeigt zu bekommen, ob dieses gerade verliehen ist"
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
    @FXML var releaseYearField: TextField = _

    loadFXML("javafx/game-search-information.fxml")

    @FXML
    def initialize(): Unit = {
      titleField.onAction = (event: ActionEvent) => inputScreen.processInput()
      authorField.onAction = (event: ActionEvent) => inputScreen.processInput()
      publisherField.onAction = (event: ActionEvent) => inputScreen.processInput()
      minimumAgeField.onAction = (event: ActionEvent) => inputScreen.processInput()
      playerCountField.onAction = (event: ActionEvent) => inputScreen.processInput()
      gameDurationField.onAction = (event: ActionEvent) => inputScreen.processInput()
      releaseYearField.onAction = (event: ActionEvent) => inputScreen.processInput()
    }

    def reset(): Unit = {
      collapsablePanel.expanded = false

      titleField.text = ""
      authorField.text = ""
      publisherField.text = ""
      minimumAgeField.text = ""
      playerCountField.text = ""
      gameDurationField.text = ""
      releaseYearField.text = ""
    }

    def toDetails: SearchDetails = SearchDetails(
      convertString(titleField.text.get),
      convertString(authorField.text.get),
      convertString(publisherField.text.get),
      convertInteger(minimumAgeField.text.get),
      convertInteger(playerCountField.text.get),
      convertInteger(gameDurationField.text.get),
      convertInteger(releaseYearField.text.get)
    )
  }

  class OutputScreen extends BorderPane with FXMLLoadable with BlankOutputFunctionScreen {
    @FXML var searchTermLabel: Label = _

    @FXML var foundGamesTableView: GameSearchTableView = _

    loadFXML("javafx/game-search-result.fxml")

    @FXML
    def initialize(): Unit = {
      searchResult.onChange(
        (observableValue, oldValue, newValue) =>
          Option(newValue) match {
            case Some(gameInformation) =>
              searchTermLabel.text = gameInformation.request.searchTerm
              foundGamesTableView.lendGameStatuses.setAll(gameInformation.foundGames: _*)

            case None =>
          }
      )
    }

    override def askForFocus(): Unit = foundGamesTableView.requestFocus()
  }

}
