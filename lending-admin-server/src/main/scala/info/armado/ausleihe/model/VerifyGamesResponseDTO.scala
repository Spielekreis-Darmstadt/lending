package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object VerifyGamesResponseDTO {
  def unapply(vgr: VerifyGamesResponseDTO): Option[(Boolean, Array[String], Array[String])] =
    Some((vgr.valid, vgr.alreadyExistingBarcodes, vgr.emptyTitleBarcodes))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class VerifyGamesResponseDTO {
  var valid: Boolean = _

  var alreadyExistingBarcodes: Array[String] = Array()

  var emptyTitleBarcodes: Array[String] = Array()
}
