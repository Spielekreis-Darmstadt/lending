/**
 *
 */
package info.armado.ausleihe.client.connection

import info.armado.ausleihe.client.model.ConnectionProperties
import info.armado.ausleihe.remote.dataobjects.information.GameInformation
import info.armado.ausleihe.remote.requests.GameInformationRequest
import info.armado.ausleihe.remote.requests.IssueGameRequest
import info.armado.ausleihe.remote.requests.IssueIdentityCardRequest
import info.armado.ausleihe.remote.requests.ReturnGameRequest
import info.armado.ausleihe.remote.requests.ReturnIdentityCardRequest
import info.armado.ausleihe.remote.results.AbstractResult
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.MediaType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import scala.util.Try

/**
 * @author marc
 *
 */
object RestServerConnection extends ServerConnectionInterface {
  private val client: Client = ClientBuilder.newClient.register(classOf[ClientExceptionFilter])

  private var server: WebTarget = _

  def connect(connectionProperties: ConnectionProperties): Unit = {
    server = client.target(connectionProperties.baseURL)
  }

  def isBarcodeInUse(barcode: String): Try[AbstractResult] = {
    Try(server.path("rest").path("info")
      .path("inuse").path(barcode)
      .request(MediaType.APPLICATION_XML)
      .get(classOf[AbstractResult]))
  }

  def getBarcodeStatus(barcode: String): Try[AbstractResult] = {
    Try(server.path("rest").path("info")
      .path("statusinformation").path(barcode)
      .request(MediaType.APPLICATION_XML)
      .get(classOf[AbstractResult]))
  }

  def searchGames(searchTerm: String, title: String, author: String, publisher:String, minimumAge: Integer, playerCount: Integer, gameDuration: Integer): Try[GameInformation] = {
    Try(server.path("rest").path("info").path("gamesinformation").request(MediaType.APPLICATION_XML)
      .post(Entity.entity(GameInformationRequest(searchTerm, title, author, publisher, playerCount, minimumAge, gameDuration), MediaType.APPLICATION_XML), classOf[GameInformation]))
  }

  def lendGames(identityCardBarcode: String, gameBarcodes: Array[String], limited: Boolean): Try[AbstractResult] = {
    Try(server.path("rest").path("issue").path("games").request(MediaType.APPLICATION_XML)
      .post(Entity.entity(IssueGameRequest(identityCardBarcode, gameBarcodes, limited), MediaType.APPLICATION_XML), classOf[AbstractResult]))
  }

  def lendIdentityCard(identityCardBarcode: String, envelopeBarcode: String): Try[AbstractResult] = {
    Try(server.path("rest").path("issue").path("identitycard").request(MediaType.APPLICATION_XML)
      .post(Entity.entity(IssueIdentityCardRequest(identityCardBarcode, envelopeBarcode), MediaType.APPLICATION_XML), classOf[AbstractResult]))
  }

  def returnGame(gameBarcode: String): Try[AbstractResult] = {
    Try(server.path("rest").path("return").path("games").request(MediaType.APPLICATION_XML)
      .post(Entity.entity(ReturnGameRequest(gameBarcode), MediaType.APPLICATION_XML), classOf[AbstractResult]))
  }

  def returnIdentityCard(identityCardBarcode: String, envelopeBarcode: String): Try[AbstractResult] = {
    Try(server.path("rest").path("return").path("identitycard").request(MediaType.APPLICATION_XML)
      .post(Entity.entity(ReturnIdentityCardRequest(identityCardBarcode, envelopeBarcode), MediaType.APPLICATION_XML), classOf[AbstractResult]))
  }
}