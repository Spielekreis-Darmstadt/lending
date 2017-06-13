package info.armado.ausleihe.remote.requests

import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlAccessType
import info.armado.ausleihe.remote.util.Annotations._

object ReturnIdentityCardRequest {
  def apply(identityCardBarcode: String, envelopeBarcode: String): ReturnIdentityCardRequest = new ReturnIdentityCardRequest(identityCardBarcode, envelopeBarcode)
  
  def unapply(returnIdentityCardRequest: ReturnIdentityCardRequest): Option[(Option[String], Option[String])] = 
    Some((Option(returnIdentityCardRequest.identityCardBarcode), Option(returnIdentityCardRequest.envelopeBarcode)))
}


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ReturnIdentityCardRequest(
  @BeanProperty var identityCardBarcode: String,
  @BeanProperty var envelopeBarcode: String) {
  
  def this() = this(null, null)
}