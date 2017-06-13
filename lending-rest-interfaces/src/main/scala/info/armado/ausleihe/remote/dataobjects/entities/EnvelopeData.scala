package info.armado.ausleihe.remote.dataobjects.entities

import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.bind.annotation.XmlAccessType
import info.armado.ausleihe.remote.util.Annotations._

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class EnvelopeData(@BeanProperty var barcode: String) extends LendingEntity {
  def this() = this(null)
}