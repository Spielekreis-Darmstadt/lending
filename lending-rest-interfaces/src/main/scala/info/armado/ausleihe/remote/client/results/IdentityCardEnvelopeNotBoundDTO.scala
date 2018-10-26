package info.armado.ausleihe.remote.client.results

import info.armado.ausleihe.remote.client.dataobjects.entities._
import info.armado.ausleihe.remote.client.util.Annotations._
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class IdentityCardEnvelopeNotBoundDTO(@BeanProperty var identityCard: IdentityCardDTO,
                                           @BeanProperty var envelope: EnvelopeDTO,
                                           @BeanProperty var trueEnvelope: EnvelopeDTO) extends AbstractResultDTO {

  def this() = this(null, null, null)
}
