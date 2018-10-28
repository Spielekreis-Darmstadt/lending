package info.armado.ausleihe.client.graphics.screen

import info.armado.ausleihe.client.connection.exceptions._
import info.armado.ausleihe.client.graphics.screen.FatalityState.Reset
import info.armado.ausleihe.client.graphics.screen.util.{FocusRequestable, Resetable}
import javafx.scene.layout.Pane

import scala.util.{Failure, Success, Try}

trait FunctionScreen extends Resetable with FocusRequestable {
  def activeScreen(screen: Pane with FocusRequestable): Unit

  def activateError(message: String, fatalityState: FatalityState): Unit

  def deactivateError(): Unit

  trait InputFunctionScreen[E] extends Resetable with FocusRequestable with InputScreen {
    def tryResult(result: Try[E]): Unit = result match {
      case Success(innerResult) => showResult(innerResult)
      case Failure(ProcessingExceptionWrapper(BadRequestException(serverAddress))) =>
        activateError(s"Bei der Kommunikation zu ${serverAddress.toString} ist etwas schief gegangen! (Es wurde ein Statuscode 400 zurückgegeben)", Reset)
      case Failure(ProcessingExceptionWrapper(NotFoundException(serverAddress))) =>
        activateError(s"Bei der Kommunikation zu ${serverAddress.toString} konnte die Addresse beim Server nicht gefunden werden! (Es wurde ein Statuscode 404 zurückgegeben)", Reset)
      case Failure(ProcessingExceptionWrapper(InternalServerErrorException(serverAddress))) =>
        activateError(s"Bei der Verarbeitung der Anfrage zu ${serverAddress.toString} ist etwas schief gegangen! (Es wurde ein Statuscode 500 zurückgegeben)", Reset)
      case Failure(ProcessingExceptionWrapper(ServerUnreachableException(serverAddress))) =>
        activateError(s"Es konnte keine Verbindung zu ${serverAddress.toString} hergestellt werden!", Reset)
      case Failure(ProcessingExceptionWrapper(UnhandledStatusCodeException(serverAddress, statusCode))) =>
        activateError(s"Es wurde der Statuscode $statusCode während der Kommunikation mit ${serverAddress.toString} zurückgegeben.", Reset)
      case Failure(exception) =>
        activateError(s"Es ist ein unbekannter Fehler aufgetreten:\n ${exception.toString}", Reset)
    }

    def showResult(result: E): Unit
  }

  trait BlankOutputFunctionScreen extends Resetable with FocusRequestable {
    def reset(): Unit = {}

    def askForFocus(): Unit = {}
  }

}