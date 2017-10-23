package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object ActivationResponseDTO {
  def apply(correctBarcodes: Array[String], incorrectBarcodes: Array[String]): ActivationResponseDTO =
    new ActivationResponseDTO(correctBarcodes, incorrectBarcodes)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ActivationResponseDTO(var correctBarcodes: Array[String],
                            var incorrectBarcodes: Array[String]) {
  def this() = this(Array(), Array())
}
