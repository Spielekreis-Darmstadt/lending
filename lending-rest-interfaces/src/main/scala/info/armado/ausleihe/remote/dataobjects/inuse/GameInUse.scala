package info.armado.ausleihe.remote.dataobjects.inuse

import javax.xml.bind.annotation.XmlRootElement
import info.armado.ausleihe.remote.dataobjects.entities.IdentityCardData
import info.armado.ausleihe.remote.dataobjects.entities.EnvelopeData
import info.armado.ausleihe.remote.util.Annotations._

@XmlRootElement
case class GameInUse( 
  @BeanProperty var identityCard: IdentityCardData,  
  @BeanProperty var envelope: EnvelopeData) extends AbstractInUseInformation {
  
  def this() = this(null, null) 
}