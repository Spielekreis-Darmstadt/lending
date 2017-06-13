package info.armado.ausleihe.remote.requests

import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlAccessType
import info.armado.ausleihe.remote.util.Annotations._

object GameInformationRequest {
  def apply(searchTerm: String): GameInformationRequest = GameInformationRequest(searchTerm, null, null, null, null, null, null)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class GameInformationRequest(
  @BeanProperty var searchTerm: String,
  @BeanProperty var searchTitle: String,
  @BeanProperty var searchAuthor: String,
  @BeanProperty var searchPublisher: String,
  @BeanProperty var playerCount: Integer,
  @BeanProperty var minimumAge: Integer,
  @BeanProperty var gameDuration: Integer) {
  
  def this() = this(null, null, null, null, null, null, null)
}