package info.armado.ausleihe.admin.transport.responses

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object AddGamesResponseDTO {
  def apply(success: Boolean): AddGamesResponseDTO = new AddGamesResponseDTO(success)

  /**
    * Creates a new [[AddGamesResponseDTO]] instance based on the given `alreadyExistingBarcodes` and `alreadyExistingBarcodes`
    * arrays.
    * The created response will be marked as successful, iff both `alreadyExistingBarcodes` and `alreadyExistingBarcodes` are empty
    *
    * @param alreadyExistingBarcodes An array containing all barcodes belonging to already existing entities in the database
    * @param emptyTitleBarcodes      An array containing all barcodes whose entries have no title set
    * @return The created [[AddGamesResponseDTO]] instance
    */
  def apply(alreadyExistingBarcodes: Array[String], duplicateBarcodes: Array[String], emptyTitleBarcodes: Array[String]): AddGamesResponseDTO =
    AddGamesResponseDTO(
      alreadyExistingBarcodes.isEmpty && duplicateBarcodes.isEmpty && emptyTitleBarcodes.isEmpty,
      alreadyExistingBarcodes, duplicateBarcodes, emptyTitleBarcodes
    )
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class AddGamesResponseDTO(var success: Boolean,
                               var alreadyExistingBarcodes: Array[String],
                               var duplicateBarcodes: Array[String],
                               var emptyTitleBarcodes: Array[String]) {
  def this() = this(false, Array(), Array(), Array())

  def this(success: Boolean) = this(success, Array(), Array(), Array())
}
