package info.armado.ausleihe.model.transport.dataobjects

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class PlayerCountDTO(var min: Integer, var max: Integer) {
  def this() = this(null, null)
}