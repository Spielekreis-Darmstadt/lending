package info.armado.ausleihe.client.transport.dataobjects.entities

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class EnvelopeDTO(var barcode: String) extends LendingEntityDTO {
  def this() = this(null)
}