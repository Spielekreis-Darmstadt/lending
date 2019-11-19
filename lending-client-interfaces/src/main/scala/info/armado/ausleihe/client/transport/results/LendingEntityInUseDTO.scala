package info.armado.ausleihe.client.transport.results

import info.armado.ausleihe.client.transport.dataobjects.entities._
import info.armado.ausleihe.client.transport.dataobjects.inuse._
import javax.xml.bind.annotation.XmlRootElement

object LendingEntityInUseDTO {
  def apply(lendingEntity: GameDTO, inUse: GameInUseDTO): LendingEntityInUseDTO =
    new LendingEntityInUseDTO(lendingEntity, inUse)

  def apply(lendingEntity: IdentityCardDTO, inUse: IdentityCardInUseDTO): LendingEntityInUseDTO =
    new LendingEntityInUseDTO(lendingEntity, inUse)

  def apply(lendingEntity: EnvelopeDTO, inUse: EnvelopeInUseDTO): LendingEntityInUseDTO =
    new LendingEntityInUseDTO(lendingEntity, inUse)

  def apply(lendingEntity: LendingEntityDTO, inUse: NotInUseDTO): LendingEntityInUseDTO =
    new LendingEntityInUseDTO(lendingEntity, inUse)
}

@XmlRootElement
case class LendingEntityInUseDTO(
    var lendingEntity: LendingEntityDTO,
    var inUse: AbstractInUseInformationDTO
) extends AbstractResultDTO {

  def this() = this(null, null)
}
