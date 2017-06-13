package info.armado.ausleihe.remote.results

import info.armado.ausleihe.remote.dataobjects.entities._
import javax.xml.bind.annotation.XmlRootElement
import info.armado.ausleihe.remote.util.Annotations._

@XmlRootElement
case class ReturnIdentityCardSuccess(
  @BeanProperty var identityCard: IdentityCardData,
  @BeanProperty var envelope: EnvelopeData) extends AbstractResult {
  
  def this() = this(null, null)
}