package info.armado.ausleihe.remote.requests

import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlAccessType
import info.armado.ausleihe.remote.util.Annotations._

object ReturnGameRequest {
  def apply(gameBarcode: String): ReturnGameRequest = new ReturnGameRequest(gameBarcode)

  def unapply(returnGameRequest: ReturnGameRequest): Option[Option[String]] = Some(Option(returnGameRequest.gameBarcode))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ReturnGameRequest(@BeanProperty var gameBarcode: String) {
  def this() = this(null)
}