package info.armado.ausleihe.client

import java.io.File

import better.files._
import info.armado.ausleihe.client.connection.RestServerConnection
import info.armado.ausleihe.client.model.{Configuration, ConnectionProperties}
import info.armado.ausleihe.client.view.javafx.AusleiheScreen
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.input.KeyCombination
import javafx.scene.paint.Color
import javafx.stage.Stage
import scopt.OptionParser

case class Config(createConfig: Boolean = false, configurationFile: File = file"connection.xml".toJava)

object Launcher {
  val parser = new OptionParser[Config]("ausleihe-client") {
    head("ausleihe-client")
    opt[Unit]('c', "create") action { (_, c) => c.copy(createConfig = true) } text ("Anzugeben, falls eine neuen ConnectionProperty-Datei erzeugt werden soll.")
    opt[File]('p', "connectionproperties") action { (x, c) => c.copy(configurationFile = x) } text ("Anzugeben, falls eine abweichende Verbindungskonfiguration verwendet werden soll")
    help("help")
  }

  def main(args: Array[String]): Unit = {
    parser.parse(args, Config()) match {
      case Some(Config(true, connectionFilePath)) =>
        ConnectionProperties.createDefault.saveToFile(connectionFilePath.toScala)
      case Some(Config(false, connectionFilePath)) =>
        connectionFilePath.exists() match {
          case true =>
            val connection = ConnectionProperties(connectionFilePath.toScala)
            Configuration.setup(connection)
            RestServerConnection.connect(connection)
            Application.launch(classOf[AusleiheApplication], args: _*)
          case false =>
            val connection = ConnectionProperties.createDefault
            Configuration.setup(connection)
            RestServerConnection.connect(connection)
            Application.launch(classOf[AusleiheApplication], args: _*)
        }
      case None => {
        // arguments are bad, error message will have been displayed
        // Dieser Fall sollte nicht eintreten
      }
    }
  }
}

class AusleiheApplication extends Application {
  def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Darmstadt Spiel Ausleihe-Client")

    val scene = new Scene(new AusleiheScreen(), 1280, 800, Color.WHITE)
    primaryStage.setScene(scene)
    primaryStage.setFullScreen(true)
    primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH)
    primaryStage.show()
  }
}