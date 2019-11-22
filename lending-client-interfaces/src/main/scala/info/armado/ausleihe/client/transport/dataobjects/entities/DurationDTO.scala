package info.armado.ausleihe.client.transport.dataobjects.entities

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object DurationDTO {
  def apply(value: Integer): DurationDTO = DurationDTO(value, value)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class DurationDTO(var min: Integer, var max: Integer) {
  def this() = this(null, null)
}
