package info.armado.ausleihe.model.transport

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class LendIdentityCardDTO(var identityCardBarcode: String,
                          var envelopeBarcode: String,
                          var lendTime: String) {
  def this() = this(null, null, null)
}
