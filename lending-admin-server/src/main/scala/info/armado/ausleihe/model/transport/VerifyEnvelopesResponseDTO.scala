package info.armado.ausleihe.model.transport

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object VerifyEnvelopesResponseDTO {
  def apply(alreadyExistingBarcodes: Array[String], duplicateBarcodes: Array[String]): VerifyEnvelopesResponseDTO =
    new VerifyEnvelopesResponseDTO(alreadyExistingBarcodes.isEmpty && duplicateBarcodes.isEmpty,
      alreadyExistingBarcodes, duplicateBarcodes)

  def unapply(ver: VerifyEnvelopesResponseDTO): Option[(Boolean, Array[String], Array[String])] =
    Some((ver.valid, ver.alreadyExistingBarcodes, ver.duplicateBarcodes))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class VerifyEnvelopesResponseDTO(var valid: Boolean,
                                 var alreadyExistingBarcodes: Array[String],
                                 var duplicateBarcodes: Array[String]) {
  def this() = this(false, Array(), Array())
}
