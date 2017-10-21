package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object ActivationResponseDTO {
  def apply(success: Boolean, invalidBarcodes: Array[String]): ActivationResponseDTO =
    new ActivationResponseDTO(success, invalidBarcodes)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ActivationResponseDTO(var success: Boolean,
                            var invalidBarcodes: Array[String]) {
  def this() = this(false, Array())
}
