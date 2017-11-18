package info.armado.ausleihe.model.transport

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object AddIdentityCardsResponseDTO {
  def apply(success: Boolean): AddIdentityCardsResponseDTO = new AddIdentityCardsResponseDTO(success)

  def apply(alreadyExistingBarcodes: Array[String], duplicateBarcodes: Array[String]): AddIdentityCardsResponseDTO =
    new AddIdentityCardsResponseDTO(alreadyExistingBarcodes.isEmpty && duplicateBarcodes.isEmpty,
      alreadyExistingBarcodes, duplicateBarcodes)

  def apply(success: Boolean, alreadyExistingBarcodes: Array[String], duplicateBarcodes: Array[String]): AddIdentityCardsResponseDTO =
    new AddIdentityCardsResponseDTO(success, alreadyExistingBarcodes, duplicateBarcodes)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class AddIdentityCardsResponseDTO(var success: Boolean,
                                  var alreadyExistingBarcodes: Array[String],
                                  var duplicateBarcodes: Array[String]) {
  def this() = this(false, Array(), Array())

  def this(success: Boolean) = this(success, Array(), Array())
}
