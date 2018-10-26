package info.armado.ausleihe.remote.client.requests

import info.armado.ausleihe.remote.client.util.Annotations._
import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object ReturnIdentityCardRequestDTO {
  def apply(identityCardBarcode: String, envelopeBarcode: String): ReturnIdentityCardRequestDTO = new ReturnIdentityCardRequestDTO(identityCardBarcode, envelopeBarcode)

  def unapply(returnIdentityCardRequest: ReturnIdentityCardRequestDTO): Option[(Option[String], Option[String])] =
    Some((Option(returnIdentityCardRequest.identityCardBarcode), Option(returnIdentityCardRequest.envelopeBarcode)))
}


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ReturnIdentityCardRequestDTO(@BeanProperty var identityCardBarcode: String,
                                   @BeanProperty var envelopeBarcode: String) {

  def this() = this(null, null)
}