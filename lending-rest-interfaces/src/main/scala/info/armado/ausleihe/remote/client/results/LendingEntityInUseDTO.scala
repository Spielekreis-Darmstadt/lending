package info.armado.ausleihe.remote.client.results

import info.armado.ausleihe.remote.client.dataobjects.entities._
import info.armado.ausleihe.remote.client.dataobjects.inuse._
import info.armado.ausleihe.remote.client.util.Annotations._
import javax.xml.bind.annotation.XmlRootElement

object LendingEntityInUseDTO {
  def apply(lendingEntity: GameDTO, inUse: GameInUseDTO): LendingEntityInUseDTO = new LendingEntityInUseDTO(lendingEntity, inUse)

  def apply(lendingEntity: IdentityCardDTO, inUse: IdentityCardInUseDTO): LendingEntityInUseDTO = new LendingEntityInUseDTO(lendingEntity, inUse)

  def apply(lendingEntity: EnvelopeDTO, inUse: EnvelopeInUseDTO): LendingEntityInUseDTO = new LendingEntityInUseDTO(lendingEntity, inUse)

  def apply(lendingEntity: LendingEntityDTO, inUse: NotInUseDTO): LendingEntityInUseDTO = new LendingEntityInUseDTO(lendingEntity, inUse)
}

@XmlRootElement
case class LendingEntityInUseDTO(@BeanProperty var lendingEntity: LendingEntityDTO,
                                 @BeanProperty var inUse: AbstractInUseInformationDTO) extends AbstractResultDTO {

  def this() = this(null, null)
}