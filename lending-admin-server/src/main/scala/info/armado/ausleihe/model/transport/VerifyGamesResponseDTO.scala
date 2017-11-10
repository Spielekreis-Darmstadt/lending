package info.armado.ausleihe.model.transport

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object VerifyGamesResponseDTO {
  def apply(alreadyExistingBarcodes: Array[String], duplicateBarcodes: Array[String], emptyTitleBarcodes: Array[String]): VerifyGamesResponseDTO =
    new VerifyGamesResponseDTO(alreadyExistingBarcodes.isEmpty && duplicateBarcodes.isEmpty && emptyTitleBarcodes.isEmpty,
      alreadyExistingBarcodes, duplicateBarcodes, emptyTitleBarcodes)

  def unapply(vgr: VerifyGamesResponseDTO): Option[(Boolean, Array[String], Array[String], Array[String])] =
    Some((vgr.valid, vgr.alreadyExistingBarcodes, vgr.duplicateBarcodes, vgr.emptyTitleBarcodes))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class VerifyGamesResponseDTO(var valid: Boolean,
                             var alreadyExistingBarcodes: Array[String],
                             var duplicateBarcodes: Array[String],
                             var emptyTitleBarcodes: Array[String]) {
  def this() = this(false, Array(), Array(), Array())
}
