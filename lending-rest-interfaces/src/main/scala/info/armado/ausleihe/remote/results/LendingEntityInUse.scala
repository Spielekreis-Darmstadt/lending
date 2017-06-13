package info.armado.ausleihe.remote.results

import info.armado.ausleihe.remote.dataobjects.entities.LendingEntity
import javax.xml.bind.annotation.XmlRootElement
import info.armado.ausleihe.remote.dataobjects.inuse.AbstractInUseInformation
import info.armado.ausleihe.remote.dataobjects.entities.GameData
import info.armado.ausleihe.remote.dataobjects.inuse.GameInUse
import info.armado.ausleihe.remote.dataobjects.entities.IdentityCardData
import info.armado.ausleihe.remote.dataobjects.inuse.IdentityCardInUse
import info.armado.ausleihe.remote.dataobjects.entities.EnvelopeData
import info.armado.ausleihe.remote.dataobjects.inuse.EnvelopeInUse
import info.armado.ausleihe.remote.dataobjects.inuse.NotInUse
import info.armado.ausleihe.remote.util.Annotations._

object LendingEntityInUse {
  def apply(lendingEntity: GameData, inUse: GameInUse) = createLendingEntityInUse(lendingEntity, inUse)
  def apply(lendingEntity: IdentityCardData, inUse: IdentityCardInUse) = createLendingEntityInUse(lendingEntity, inUse)
  def apply(lendingEntity: EnvelopeData, inUse: EnvelopeInUse) = createLendingEntityInUse(lendingEntity, inUse)
  def apply(lendingEntity: LendingEntity, inUse: NotInUse) = createLendingEntityInUse(lendingEntity, inUse)

  private def createLendingEntityInUse(lendingEntity: LendingEntity, inUse: AbstractInUseInformation) = new LendingEntityInUse(lendingEntity, inUse)
}

@XmlRootElement
case class LendingEntityInUse(
    @BeanProperty var lendingEntity: LendingEntity,
    @BeanProperty var inUse: AbstractInUseInformation) extends AbstractResult {

  def this() = this(null, null)
}