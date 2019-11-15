/**
  *
  */
package info.armado.ausleihe.client.connection

import info.armado.ausleihe.client.model.ConnectionProperties
import info.armado.ausleihe.client.model.SearchDetails
import info.armado.ausleihe.client.transport.dataobjects.information.GameInformationDTO
import info.armado.ausleihe.client.transport.requests._
import info.armado.ausleihe.client.transport.results.AbstractResultDTO
import javax.ws.rs.client.{Client, ClientBuilder, Entity, WebTarget}
import javax.ws.rs.core.MediaType

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

  def isBarcodeInUse(barcode: String): Try[AbstractResultDTO] = {
    Try(
      server
        .path("rest")
        .path("info")
        .path("inuse")
        .path(barcode)
        .request(MediaType.APPLICATION_XML)
        .get(classOf[AbstractResultDTO])
    )
  }

  def getBarcodeStatus(barcode: String): Try[AbstractResultDTO] = {
    Try(
      server
        .path("rest")
        .path("info")
        .path("statusinformation")
        .path(barcode)
        .request(MediaType.APPLICATION_XML)
        .get(classOf[AbstractResultDTO])
    )
  }

  def searchGames(searchTerm: String, searchDetails: SearchDetails): Try[GameInformationDTO] = {
    Try(
      server
        .path("rest")
        .path("info")
        .path("gamesinformation")
        .request(MediaType.APPLICATION_XML)
        .post(
          Entity.entity(
            GameInformationRequestDTO(
              searchTerm,
              searchDetails.title.orNull,
              searchDetails.author.orNull,
              searchDetails.publisher.orNull,
              searchDetails.playerCount.orNull,
              searchDetails.minimumAge.orNull,
              searchDetails.gameDuration.orNull,
              searchDetails.releaseYear.orNull
            ),
            MediaType.APPLICATION_XML
          ),
          classOf[GameInformationDTO]
        )
    )
  }

  def lendGames(
      identityCardBarcode: String,
      gameBarcodes: Array[String],
      limited: Boolean
  ): Try[AbstractResultDTO] = {
    Try(
      server
        .path("rest")
        .path("issue")
        .path("games")
        .request(MediaType.APPLICATION_XML)
        .post(
          Entity.entity(
            IssueGameRequestDTO(identityCardBarcode, gameBarcodes, limited),
            MediaType.APPLICATION_XML
          ),
          classOf[AbstractResultDTO]
        )
    )
  }

  def lendIdentityCard(
      identityCardBarcode: String,
      envelopeBarcode: String
  ): Try[AbstractResultDTO] = {
    Try(
      server
        .path("rest")
        .path("issue")
        .path("identitycard")
        .request(MediaType.APPLICATION_XML)
        .post(
          Entity.entity(
            IssueIdentityCardRequestDTO(identityCardBarcode, envelopeBarcode),
            MediaType.APPLICATION_XML
          ),
          classOf[AbstractResultDTO]
        )
    )
  }

  def returnGame(gameBarcode: String): Try[AbstractResultDTO] = {
    Try(
      server
        .path("rest")
        .path("return")
        .path("games")
        .request(MediaType.APPLICATION_XML)
        .post(
          Entity.entity(ReturnGameRequestDTO(gameBarcode), MediaType.APPLICATION_XML),
          classOf[AbstractResultDTO]
        )
    )
  }

  def returnIdentityCard(
      identityCardBarcode: String,
      envelopeBarcode: String
  ): Try[AbstractResultDTO] = {
    Try(
      server
        .path("rest")
        .path("return")
        .path("identitycard")
        .request(MediaType.APPLICATION_XML)
        .post(
          Entity.entity(
            ReturnIdentityCardRequestDTO(identityCardBarcode, envelopeBarcode),
            MediaType.APPLICATION_XML
          ),
          classOf[AbstractResultDTO]
        )
    )
  }
}
