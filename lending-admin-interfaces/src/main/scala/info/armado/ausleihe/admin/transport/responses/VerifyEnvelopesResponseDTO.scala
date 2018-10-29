package info.armado.ausleihe.admin.transport.responses

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object VerifyEnvelopesResponseDTO {
  def apply(alreadyExistingBarcodes: Array[String], duplicateBarcodes: Array[String]): VerifyEnvelopesResponseDTO =
    VerifyEnvelopesResponseDTO(alreadyExistingBarcodes.isEmpty && duplicateBarcodes.isEmpty, alreadyExistingBarcodes, duplicateBarcodes)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class VerifyEnvelopesResponseDTO(var valid: Boolean,
                                      var alreadyExistingBarcodes: Array[String],
                                      var duplicateBarcodes: Array[String]) {
  def this() = this(false, Array(), Array())
}
