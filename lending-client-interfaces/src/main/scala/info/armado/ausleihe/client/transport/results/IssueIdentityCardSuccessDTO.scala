package info.armado.ausleihe.client.transport.results

import info.armado.ausleihe.client.transport.dataobjects.entities._
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class IssueIdentityCardSuccessDTO(var identityCard: IdentityCardDTO, var envelope: EnvelopeDTO)
    extends AbstractResultDTO {

  def this() = this(null, null)
}
