package info.armado.ausleihe.remote.client.dataobjects.inuse

import info.armado.ausleihe.remote.client.dataobjects.entities._
import info.armado.ausleihe.remote.client.util.Annotations._
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class GameInUseDTO(
                         @BeanProperty var identityCard: IdentityCardDTO,
                         @BeanProperty var envelope: EnvelopeDTO) extends AbstractInUseInformationDTO {

  def this() = this(null, null)
}