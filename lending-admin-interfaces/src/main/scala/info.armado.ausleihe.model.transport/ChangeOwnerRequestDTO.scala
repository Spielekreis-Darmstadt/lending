package info.armado.ausleihe.model.transport

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object ChangeOwnerRequestDTO {
  def apply(identityCardBarcodeString: String, owner: String): ChangeOwnerRequestDTO =
    new ChangeOwnerRequestDTO(identityCardBarcodeString, owner)

  def unapply(changeOwnerRequest: ChangeOwnerRequestDTO): Option[(String, String)] =
    Some((changeOwnerRequest.identityCardBarcodeString, changeOwnerRequest.owner))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ChangeOwnerRequestDTO(var identityCardBarcodeString: String,
                            var owner: String) {
  def this() = this(null, null)
}
