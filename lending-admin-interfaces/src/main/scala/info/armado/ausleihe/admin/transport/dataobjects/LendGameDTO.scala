package info.armado.ausleihe.admin.transport.dataobjects

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class LendGameDTO(var barcode: String,
                       var lendTime: String) {
  def this() = this(null, null)
}
