package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object ExistsResponseDTO {
  def apply(input: String, exists: Boolean): ExistsResponseDTO =
    new ExistsResponseDTO(input, exists)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ExistsResponseDTO(var input: String,
                        var exists: Boolean) {
  def this() = this(null, false)
}
