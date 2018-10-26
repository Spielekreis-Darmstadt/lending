package info.armado.ausleihe.admin.transport.dataobjects

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class IdentityCardDTO(var barcode: String,
                           var activated: Boolean) {
  def this() = this(null, false)
}
