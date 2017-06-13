package info.armado.ausleihe.remote.requests

import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlAccessType
import info.armado.ausleihe.remote.util.Annotations._

object IssueIdentityCardRequest {
  def apply(identityCardBarcode: String, envelopeBarcode: String): IssueIdentityCardRequest = 
    new IssueIdentityCardRequest(identityCardBarcode, envelopeBarcode)

  def unapply(issueIdentityCardRequest: IssueIdentityCardRequest): Option[(Option[String], Option[String])] = 
    Some((Option(issueIdentityCardRequest.identityCardBarcode), Option(issueIdentityCardRequest.envelopeBarcode)))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class IssueIdentityCardRequest(
  @BeanProperty var identityCardBarcode: String,
  @BeanProperty var envelopeBarcode: String) {
  
  def this() = this(null, null)
}