package info.armado.ausleihe.client.transport.requests

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object ReturnIdentityCardRequestDTO {
  def apply(identityCardBarcode: String, envelopeBarcode: String): ReturnIdentityCardRequestDTO =
    new ReturnIdentityCardRequestDTO(identityCardBarcode, envelopeBarcode)

  def unapply(
      returnIdentityCardRequest: ReturnIdentityCardRequestDTO
  ): Option[(Option[String], Option[String])] =
    Some(
      (
        Option(returnIdentityCardRequest.identityCardBarcode),
        Option(returnIdentityCardRequest.envelopeBarcode)
      )
    )
}
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ReturnIdentityCardRequestDTO(var identityCardBarcode: String, var envelopeBarcode: String) {

  def this() = this(null, null)
}
