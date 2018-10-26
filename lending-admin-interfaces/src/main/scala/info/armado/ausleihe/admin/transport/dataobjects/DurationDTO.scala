package info.armado.ausleihe.admin.transport.dataobjects

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class DurationDTO(var min: Integer, var max: Integer) {
  def this() = this(null, null)
}