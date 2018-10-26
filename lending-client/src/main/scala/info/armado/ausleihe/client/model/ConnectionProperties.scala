package info.armado.ausleihe.client.model

import better.files.File
import scala.xml.XML
import scala.xml.PrettyPrinter

object ConnectionProperties {
  def apply(propertiesFile: File): ConnectionProperties = {
    val contentNode = XML.loadFile(propertiesFile.toJava)

    val url = (contentNode \ "url").text
    val port = (contentNode \ "port").text.toInt
    val basePath = (contentNode \ "basepath").text
    val operator = Operators.parse((contentNode \ "operator").text)

    ConnectionProperties(url, port, basePath, operator)
  }

  def createDefault = ConnectionProperties("localhost", 8080, "lending-rest-server", Some(Operators.Spielekreis))
}

case class ConnectionProperties(val url: String, val port: Int, val basePath: String, operator: Option[Operators.Value]) {
  val baseURL = s"http://${url}:${port}/${basePath}"

  def saveToFile(propertiesFile: File) = {
    val content =
      <connection>
        <url>{ url }</url>
        <port>{ port }</port>
        <basepath>{ basePath }</basepath>
        <operator>{
          operator match {
            case Some(operator) => operator.toString.toLowerCase
            case None => "unknown"
          }
        }</operator>
      </connection>

    val prettyPrinter = new PrettyPrinter(80, 2)
    propertiesFile.overwrite(prettyPrinter.format(content))
  }
}