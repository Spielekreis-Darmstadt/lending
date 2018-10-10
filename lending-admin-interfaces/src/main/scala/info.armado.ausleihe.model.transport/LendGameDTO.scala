package info.armado.ausleihe.model.transport

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class LendGameDTO(var barcode: String,
                  var lendTime: String) {
  def this() = this(null, null)
}
