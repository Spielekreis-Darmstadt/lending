package info.armado.ausleihe.remote.client.requests

import info.armado.ausleihe.remote.client.util.Annotations._
import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object GameInformationRequestDTO {
  def apply(searchTerm: String): GameInformationRequestDTO = GameInformationRequestDTO(searchTerm, null, null, null, null, null, null)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class GameInformationRequestDTO(@BeanProperty var searchTerm: String,
                                     @BeanProperty var searchTitle: String,
                                     @BeanProperty var searchAuthor: String,
                                     @BeanProperty var searchPublisher: String,
                                     @BeanProperty var playerCount: Integer,
                                     @BeanProperty var minimumAge: Integer,
                                     @BeanProperty var gameDuration: Integer) {

  def this() = this(null, null, null, null, null, null, null)
}