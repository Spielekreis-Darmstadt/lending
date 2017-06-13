package info.armado.ausleihe.remote.results

import javax.xml.bind.annotation.XmlRootElement
import info.armado.ausleihe.remote.dataobjects.entities.IdentityCardData
import info.armado.ausleihe.remote.dataobjects.entities.EnvelopeData
import info.armado.ausleihe.remote.util.Annotations._

@XmlRootElement
case class IssueIdentityCardSuccess(
  @BeanProperty var identityCard: IdentityCardData,  
  @BeanProperty var envelope: EnvelopeData) extends AbstractResult {
  
  def this() = this(null, null)
}