package info.armado.ausleihe.client.transport.requests

import info.armado.ausleihe.client.transport.util.Annotations._
import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object IssueGameRequestDTO {
  def apply(identityCardBarcode: String, gameBarcodes: Array[String], limited: Boolean): IssueGameRequestDTO =
    new IssueGameRequestDTO(identityCardBarcode, gameBarcodes, limited)

  def unapply(issueGameRequest: IssueGameRequestDTO): Option[(Option[String], Array[String], Option[Boolean])] =
    Some((Option(issueGameRequest.identityCardBarcode), issueGameRequest.gameBarcodes, Option(issueGameRequest.limited)))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class IssueGameRequestDTO(@BeanProperty var identityCardBarcode: String,
                          @BeanProperty var gameBarcodes: Array[String],
                          @BeanProperty var limited: Boolean) {

  def this() = this(null, Array.empty, false)
}