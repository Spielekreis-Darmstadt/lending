package info.armado.ausleihe.model.transport.responses

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object AddEnvelopesResponseDTO {
  def apply(success: Boolean): AddEnvelopesResponseDTO = new AddEnvelopesResponseDTO(success)

  def apply(alreadyExistingBarcodes: Array[String], duplicateBarcodes: Array[String]): AddEnvelopesResponseDTO =
    AddEnvelopesResponseDTO(alreadyExistingBarcodes.isEmpty && duplicateBarcodes.isEmpty, alreadyExistingBarcodes, duplicateBarcodes)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class AddEnvelopesResponseDTO(var success: Boolean,
                                   var alreadyExistingBarcodes: Array[String],
                                   var duplicateBarcodes: Array[String]) {
  def this() = this(false, Array(), Array())

  def this(success: Boolean) = this(success, Array(), Array())
}
