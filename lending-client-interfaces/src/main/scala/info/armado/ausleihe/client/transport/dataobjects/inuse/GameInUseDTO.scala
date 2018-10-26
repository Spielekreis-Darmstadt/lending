package info.armado.ausleihe.client.transport.dataobjects.inuse

import info.armado.ausleihe.client.transport.dataobjects.entities._
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class GameInUseDTO(
                         var identityCard: IdentityCardDTO,
                         var envelope: EnvelopeDTO) extends AbstractInUseInformationDTO {

  def this() = this(null, null)
}