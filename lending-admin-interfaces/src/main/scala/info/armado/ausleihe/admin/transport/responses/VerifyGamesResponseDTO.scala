package info.armado.ausleihe.admin.transport.responses

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object VerifyGamesResponseDTO {
  def apply(alreadyExistingBarcodes: Array[String], duplicateBarcodes: Array[String], emptyTitleBarcodes: Array[String]): VerifyGamesResponseDTO =
    VerifyGamesResponseDTO(
      alreadyExistingBarcodes.isEmpty && duplicateBarcodes.isEmpty && emptyTitleBarcodes.isEmpty,
      alreadyExistingBarcodes, duplicateBarcodes, emptyTitleBarcodes
    )
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class VerifyGamesResponseDTO(var valid: Boolean,
                                  var alreadyExistingBarcodes: Array[String],
                                  var duplicateBarcodes: Array[String],
                                  var emptyTitleBarcodes: Array[String]) {
  def this() = this(false, Array(), Array(), Array())
}
