package info.armado.ausleihe.client.transport.requests

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object GameInformationRequestDTO {
  def apply(searchTerm: String): GameInformationRequestDTO = GameInformationRequestDTO(searchTerm, null, null, null, null, null, null)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class GameInformationRequestDTO(var searchTerm: String,
                                     var searchTitle: String,
                                     var searchAuthor: String,
                                     var searchPublisher: String,
                                     var playerCount: Integer,
                                     var minimumAge: Integer,
                                     var gameDuration: Integer) {

  def this() = this(null, null, null, null, null, null, null)
}