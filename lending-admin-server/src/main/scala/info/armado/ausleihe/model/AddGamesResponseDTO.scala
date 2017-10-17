package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object AddGamesResponseDTO {
  def apply(success: Boolean): AddGamesResponseDTO = {
    val result = new AddGamesResponseDTO

    result.success = success

    result
  }

  def apply(success: Boolean, alreadyExistingBarcodes: Array[String], emptyTitleBarcodes: Array[String]): AddGamesResponseDTO = {
    val result = new AddGamesResponseDTO

    result.success = success
    result.alreadyExistingBarcodes = alreadyExistingBarcodes
    result.emptyTitleBarcodes = emptyTitleBarcodes

    result
  }
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class AddGamesResponseDTO {
  var success: Boolean = _

  var alreadyExistingBarcodes: Array[String] = Array()

  var emptyTitleBarcodes: Array[String] = Array()
}
