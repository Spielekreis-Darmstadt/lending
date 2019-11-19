package info.armado.ausleihe.client.transport.results

import info.armado.ausleihe.client.transport.dataobjects.entities._
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class IdentityCardEnvelopeNotBoundDTO(
    var identityCard: IdentityCardDTO,
    var envelope: EnvelopeDTO,
    var trueEnvelope: EnvelopeDTO
) extends AbstractResultDTO {

  def this() = this(null, null, null)
}
