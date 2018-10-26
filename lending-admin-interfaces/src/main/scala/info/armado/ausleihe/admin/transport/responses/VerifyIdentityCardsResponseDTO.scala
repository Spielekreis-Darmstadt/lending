package info.armado.ausleihe.admin.transport.responses

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object VerifyIdentityCardsResponseDTO {
  def apply(alreadyExistingBarcodes: Array[String], duplicateBarcodes: Array[String]): VerifyIdentityCardsResponseDTO =
    VerifyIdentityCardsResponseDTO(alreadyExistingBarcodes.isEmpty && duplicateBarcodes.isEmpty, alreadyExistingBarcodes, duplicateBarcodes)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class VerifyIdentityCardsResponseDTO(var valid: Boolean,
                                          var alreadyExistingBarcodes: Array[String],
                                          var duplicateBarcodes: Array[String]) {
  def this() = this(false, Array(), Array())
}
