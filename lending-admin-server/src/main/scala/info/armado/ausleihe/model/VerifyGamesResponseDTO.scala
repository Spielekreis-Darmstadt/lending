package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class VerifyGamesResponseDTO {
  var valid: Boolean = _

  var alreadyExistingBarcodes: Array[String] = Array()

  var emptyTitleBarcodes: Array[String] = Array()
}
