package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object VerifyEnvelopesResponseDTO {
  def apply(alreadyExistingBarcodes: Array[String]): VerifyEnvelopesResponseDTO =
    new VerifyEnvelopesResponseDTO(alreadyExistingBarcodes.isEmpty, alreadyExistingBarcodes)

  def unapply(ver: VerifyEnvelopesResponseDTO): Option[(Boolean, Array[String])] =
    Some((ver.valid, ver.alreadyExistingBarcodes))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class VerifyEnvelopesResponseDTO(var valid: Boolean,
                                 var alreadyExistingBarcodes: Array[String]) {
  def this() = this(false, Array())
}
