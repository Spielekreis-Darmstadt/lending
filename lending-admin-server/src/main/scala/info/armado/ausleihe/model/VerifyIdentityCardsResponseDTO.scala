package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object VerifyIdentityCardsResponseDTO {
  def apply(alreadyExistingBarcodes: Array[String]): VerifyIdentityCardsResponseDTO =
    new VerifyIdentityCardsResponseDTO(alreadyExistingBarcodes.isEmpty, alreadyExistingBarcodes)

  def unapply(vicr: VerifyIdentityCardsResponseDTO): Option[(Boolean, Array[String])] =
    Some((vicr.valid, vicr.alreadyExistingBarcodes))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class VerifyIdentityCardsResponseDTO(var valid: Boolean,
                                     var alreadyExistingBarcodes: Array[String]) {
  def this() = this(false, Array())
}
