package info.armado.ausleihe.client.transport.results

import info.armado.ausleihe.client.transport.dataobjects.entities._
import info.armado.ausleihe.client.transport.util.Annotations._
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class IssueIdentityCardSuccessDTO(@BeanProperty var identityCard: IdentityCardDTO,
                                       @BeanProperty var envelope: EnvelopeDTO) extends AbstractResultDTO {

  def this() = this(null, null)
}