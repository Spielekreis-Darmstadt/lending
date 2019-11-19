package info.armado.ausleihe.client.transport.dataobjects.entities

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object PlayerCountDTO {
  def apply(value: Integer): PlayerCountDTO = PlayerCountDTO(value, value)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class PlayerCountDTO(var min: Integer, var max: Integer) {
  def this() = this(null, null)

  def this(value: Integer) = this(value, value)
}
