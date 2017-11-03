package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object AddEnvelopesResponseDTO {
  def apply(success: Boolean): AddEnvelopesResponseDTO = new AddEnvelopesResponseDTO(success)

  def apply(alreadyExistingBarcodes: Array[String]): AddEnvelopesResponseDTO =
    new AddEnvelopesResponseDTO(alreadyExistingBarcodes.isEmpty, alreadyExistingBarcodes)

  def apply(success: Boolean, alreadyExistingBarcodes: Array[String]): AddEnvelopesResponseDTO =
    new AddEnvelopesResponseDTO(success, alreadyExistingBarcodes)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class AddEnvelopesResponseDTO(var success: Boolean,
                              var alreadyExistingBarcodes: Array[String]) {
  def this() = this(false, Array())

  def this(success: Boolean) = this(success, Array())
}
