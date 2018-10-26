package info.armado.ausleihe.model.transport.responses

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object AddIdentityCardsResponseDTO {
  def apply(success: Boolean): AddIdentityCardsResponseDTO = new AddIdentityCardsResponseDTO(success)

  def apply(alreadyExistingBarcodes: Array[String], duplicateBarcodes: Array[String]): AddIdentityCardsResponseDTO =
    AddIdentityCardsResponseDTO(alreadyExistingBarcodes.isEmpty && duplicateBarcodes.isEmpty, alreadyExistingBarcodes, duplicateBarcodes)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class AddIdentityCardsResponseDTO(var success: Boolean,
                                       var alreadyExistingBarcodes: Array[String],
                                       var duplicateBarcodes: Array[String]) {
  def this() = this(false, Array(), Array())

  def this(success: Boolean) = this(success, Array(), Array())
}
