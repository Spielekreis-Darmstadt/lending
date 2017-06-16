package info.armado.ausleihe.database.dataobjects

import info.armado.ausleihe.database.enums.Prefix
import scala.beans.BeanProperty

object ValidateBarcode {
  def apply(str: String): ValidateBarcode = if (!Barcode.valid(str)) InvalidBarcode(str) else ValidBarcode(Barcode(str))
}
abstract class ValidateBarcode
case class ValidBarcode(barcode: Barcode) extends ValidateBarcode
case class InvalidBarcode(barcode: String) extends ValidateBarcode

object Barcode {
  def apply(str: String): Barcode = new Barcode(str)

  def calculateChecksum(str: String): String = Option(str) match {
    case None | Some("") => "*"
    case Some(str) => {
      val a1 = str(0) - 48
      val a2 = str(1) - 48
      val a3 = str(2) - 48
      val a4 = str(3) - 48
      val a5 = str(4) - 48
      val a6 = str(5) - 48
      val a7 = str(6) - 48

      var prz = 0
      var r3 = 0

      prz = a1 + a3 + a5 + a7
      r3 = 2 * a2
      prz += r3 % 10 + r3 / 10;
      r3 = 2 * a4;
      prz += r3 % 10 + r3 / 10;
      r3 = 2 * a6;
      prz += r3 % 10 + r3 / 10;

      prz = prz % 10;

      // negative Prüfsumme => schlecht
      if (prz < 0) {
        prz = prz * (-1);
      }

      return prz.toString();
    }
  }

  def calculateChecksum(prefix: Prefix, counter: String): String = calculateChecksum(prefix.getPrefix + counter)

  def valid(str: String): Boolean = Option(str) match {
    case None | Some("") => false
    case Some(x) if x.length() != 8 => false
    case Some(x) => new Barcode(x) match {
      case Barcode(Prefix.Any | Prefix.Unknown, counter, checksum) => false
      case barcode => barcode.valid
    }
  }

  def fill(counter: Int, numberOfDigits: Int): String = {
    var result = counter.toString()

    while (result.length() < numberOfDigits) result = "0" + result

    result
  }

  def createWildcard: Barcode = new Barcode(Prefix.Any, "_____", "_")

  def createWildcard(prefix: Prefix) = new Barcode(prefix, "_____", "_")
}

case class Barcode(@BeanProperty val prefix: Prefix, @BeanProperty val counter: String, @BeanProperty val checksum: String) {
  def this(prefix: String, counter: String, checksum: String) = this(Prefix.parse(prefix), counter, checksum)
  def this(barcode: String) = this(barcode.substring(0, 2), barcode.substring(2, 7), barcode.substring(7, 8))
  def this(prefix: Prefix, counter: String) = this(prefix, counter, Barcode.calculateChecksum(prefix, counter))
  def this(prefix: Prefix, counter: Int) = this(prefix, Barcode.fill(counter, 5))

  def valid: Boolean = Barcode.calculateChecksum(prefix, counter) match {
    case `checksum` => true
    case _ => false
  }

  override def toString: String = prefix.getPrefix + counter + checksum
}