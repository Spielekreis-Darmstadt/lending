package info.armado.ausleihe.remote.requests

import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlAccessType
import info.armado.ausleihe.remote.util.Annotations._

object IssueGameRequest {
  def apply(identityCardBarcode: String, gameBarcodes: Array[String], limited: Boolean): IssueGameRequest = 
    new IssueGameRequest(identityCardBarcode, gameBarcodes, limited)
  
  def unapply(issueGameRequest: IssueGameRequest): Option[(Option[String], Array[String], Option[Boolean])] = 
    Some((Option(issueGameRequest.identityCardBarcode), issueGameRequest.gameBarcodes, Option(issueGameRequest.limited)))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class IssueGameRequest(
  @BeanProperty var identityCardBarcode: String,
  @BeanProperty var gameBarcodes: Array[String],
  @BeanProperty var limited: Boolean) {
  
  def this() = this(null, Array.empty, false)
}