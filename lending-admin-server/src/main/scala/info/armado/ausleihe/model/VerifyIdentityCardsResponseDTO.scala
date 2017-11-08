package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object VerifyIdentityCardsResponseDTO {
  def apply(alreadyExistingBarcodes: Array[String], duplicateBarcodes: Array[String]): VerifyIdentityCardsResponseDTO =
    new VerifyIdentityCardsResponseDTO(alreadyExistingBarcodes.isEmpty && duplicateBarcodes.isEmpty,
      alreadyExistingBarcodes, duplicateBarcodes)

  def unapply(vicr: VerifyIdentityCardsResponseDTO): Option[(Boolean, Array[String], Array[String])] =
    Some((vicr.valid, vicr.alreadyExistingBarcodes, vicr.duplicateBarcodes))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class VerifyIdentityCardsResponseDTO(var valid: Boolean,
                                     var alreadyExistingBarcodes: Array[String],
                                     var duplicateBarcodes: Array[String]) {
  def this() = this(false, Array(), Array())
}
