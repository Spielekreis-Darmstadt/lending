package info.armado.ausleihe.remote.results

import info.armado.ausleihe.remote.dataobjects.entities.IdentityCardData
import info.armado.ausleihe.remote.dataobjects.entities.EnvelopeData
import javax.xml.bind.annotation.XmlRootElement
import info.armado.ausleihe.remote.util.Annotations._

@XmlRootElement
case class IdentityCardEnvelopeNotBound(  
  @BeanProperty var identityCard: IdentityCardData,
  @BeanProperty var envelope: EnvelopeData,
  @BeanProperty var trueEnvelope: EnvelopeData) extends AbstractResult {
  
  def this() = this(null, null, null)
}