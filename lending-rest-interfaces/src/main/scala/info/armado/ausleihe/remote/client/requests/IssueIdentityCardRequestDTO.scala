package info.armado.ausleihe.remote.client.requests

import info.armado.ausleihe.remote.client.util.Annotations._
import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object IssueIdentityCardRequestDTO {
  def apply(identityCardBarcode: String, envelopeBarcode: String): IssueIdentityCardRequestDTO =
    new IssueIdentityCardRequestDTO(identityCardBarcode, envelopeBarcode)

  def unapply(issueIdentityCardRequest: IssueIdentityCardRequestDTO): Option[(Option[String], Option[String])] =
    Some((Option(issueIdentityCardRequest.identityCardBarcode), Option(issueIdentityCardRequest.envelopeBarcode)))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class IssueIdentityCardRequestDTO(@BeanProperty var identityCardBarcode: String,
                                  @BeanProperty var envelopeBarcode: String) {

  def this() = this(null, null)
}