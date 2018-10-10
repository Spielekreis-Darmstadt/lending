package info.armado.ausleihe.model.transport

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object AddEnvelopesResponseDTO {
  def apply(success: Boolean): AddEnvelopesResponseDTO = new AddEnvelopesResponseDTO(success)

  def apply(alreadyExistingBarcodes: Array[String], duplicateBarcodes: Array[String]): AddEnvelopesResponseDTO =
    new AddEnvelopesResponseDTO(alreadyExistingBarcodes.isEmpty && duplicateBarcodes.isEmpty,
      alreadyExistingBarcodes, duplicateBarcodes)

  def apply(success: Boolean, alreadyExistingBarcodes: Array[String], duplicateBarcodes: Array[String]): AddEnvelopesResponseDTO =
    new AddEnvelopesResponseDTO(success, alreadyExistingBarcodes, duplicateBarcodes)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class AddEnvelopesResponseDTO(var success: Boolean,
                              var alreadyExistingBarcodes: Array[String],
                              var duplicateBarcodes: Array[String]) {
  def this() = this(false, Array(), Array())

  def this(success: Boolean) = this(success, Array(), Array())
}
