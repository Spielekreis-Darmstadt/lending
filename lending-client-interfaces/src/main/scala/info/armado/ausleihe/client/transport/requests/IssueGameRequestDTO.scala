package info.armado.ausleihe.client.transport.requests

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object IssueGameRequestDTO {
  def apply(
      identityCardBarcode: String,
      gameBarcodes: Array[String],
      limited: Boolean
  ): IssueGameRequestDTO =
    new IssueGameRequestDTO(identityCardBarcode, gameBarcodes, limited)

  def unapply(
      issueGameRequest: IssueGameRequestDTO
  ): Option[(Option[String], Array[String], Option[Boolean])] =
    Some(
      (
        Option(issueGameRequest.identityCardBarcode),
        issueGameRequest.gameBarcodes,
        Option(issueGameRequest.limited)
      )
    )
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class IssueGameRequestDTO(
    var identityCardBarcode: String,
    var gameBarcodes: Array[String],
    var limited: Boolean
) {

  def this() = this(null, Array.empty, false)
}
