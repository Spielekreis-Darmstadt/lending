package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object AddGamesResponseDTO {
  def apply(success: Boolean): AddGamesResponseDTO = new AddGamesResponseDTO(success)

  def apply(success: Boolean, alreadyExistingBarcodes: Array[String], emptyTitleBarcodes: Array[String]): AddGamesResponseDTO =
    new AddGamesResponseDTO(success, alreadyExistingBarcodes, emptyTitleBarcodes)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class AddGamesResponseDTO(var success: Boolean,
                          var alreadyExistingBarcodes: Array[String],
                          var emptyTitleBarcodes: Array[String]) {
  def this() = this(false, Array(), Array())
  def this(success: Boolean) = this(success, Array(), Array())
}
