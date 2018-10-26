package info.armado.ausleihe.remote.client.results

import info.armado.ausleihe.remote.client.dataobjects.entities._
import info.armado.ausleihe.remote.client.util.Annotations._
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class IssueIdentityCardSuccessDTO(@BeanProperty var identityCard: IdentityCardDTO,
                                       @BeanProperty var envelope: EnvelopeDTO) extends AbstractResultDTO {

  def this() = this(null, null)
}