package info.armado.ausleihe.client.transport.dataobjects.entities

import info.armado.ausleihe.client.transport.util.Annotations._
import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class EnvelopeDTO(@BeanProperty var barcode: String) extends LendingEntityDTO {
  def this() = this(null)
}