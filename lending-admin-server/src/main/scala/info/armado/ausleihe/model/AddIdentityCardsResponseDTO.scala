package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object AddIdentityCardsResponseDTO {
  def apply(success: Boolean): AddIdentityCardsResponseDTO = new AddIdentityCardsResponseDTO(success)

  def apply(alreadyExistingBarcodes: Array[String]): AddIdentityCardsResponseDTO =
    new AddIdentityCardsResponseDTO(alreadyExistingBarcodes.isEmpty, alreadyExistingBarcodes)

  def apply(success: Boolean, alreadyExistingBarcodes: Array[String]): AddIdentityCardsResponseDTO =
    new AddIdentityCardsResponseDTO(success, alreadyExistingBarcodes)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class AddIdentityCardsResponseDTO(var success: Boolean,
                                  var alreadyExistingBarcodes: Array[String]) {
  def this() = this(false, Array())

  def this(success: Boolean) = this(success, Array())
}
