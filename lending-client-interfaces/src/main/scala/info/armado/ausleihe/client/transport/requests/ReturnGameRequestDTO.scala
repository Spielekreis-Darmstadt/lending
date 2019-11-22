package info.armado.ausleihe.client.transport.requests

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object ReturnGameRequestDTO {
  def apply(gameBarcode: String): ReturnGameRequestDTO = new ReturnGameRequestDTO(gameBarcode)

  def unapply(returnGameRequest: ReturnGameRequestDTO): Option[Option[String]] =
    Some(Option(returnGameRequest.gameBarcode))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ReturnGameRequestDTO(var gameBarcode: String) {
  def this() = this(null)
}
