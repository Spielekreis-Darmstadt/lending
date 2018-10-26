package info.armado.ausleihe.remote.client.requests

import info.armado.ausleihe.remote.client.util.Annotations._
import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object ReturnGameRequestDTO {
  def apply(gameBarcode: String): ReturnGameRequestDTO = new ReturnGameRequestDTO(gameBarcode)

  def unapply(returnGameRequest: ReturnGameRequestDTO): Option[Option[String]] = Some(Option(returnGameRequest.gameBarcode))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ReturnGameRequestDTO(@BeanProperty var gameBarcode: String) {
  def this() = this(null)
}