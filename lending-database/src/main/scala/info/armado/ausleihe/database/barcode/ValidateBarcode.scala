package info.armado.ausleihe.database.barcode

/**
  * Factory for [[ValidateBarcode]] instances.
  */
object ValidateBarcode {
  /**
    * Creates a new ValidateBarcode instance depending on the given string.
    * If this given string contains a valid barcode a [[ValidBarcode]] instance is returned, otherwise
    * an [[InvalidBarcode]] instance is returned.
    *
    * @param str The barcode string to be validated
    * @return The new ValidateBarcode instance
    */
  def apply(str: String): ValidateBarcode = if (!Barcode.valid(str)) InvalidBarcode(str) else ValidBarcode(Barcode(str))
}

/**
  * An abstract ValidateBarcode used to check if a given barcode string is valid.
  */
abstract class ValidateBarcode

/**
  * A valid barcode
  *
  * @constructor Creates a new ValidBarcode instance
  * @param barcode The barcode created by parsing a given valid barcode string
  */
case class ValidBarcode(barcode: Barcode) extends ValidateBarcode

/**
  * An invalid barcode
  *
  * @constructor Creates a new InvalidBarcode instance
  * @param barcode The invalid barcode string
  */
case class InvalidBarcode(barcode: String) extends ValidateBarcode
