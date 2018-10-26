package info.armado.ausleihe.model.transport.requests

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class ChangeOwnerRequestDTO(var identityCardBarcodeString: String,
                                 var owner: String) {
  def this() = this(null, null)
}
