package info.armado.ausleihe.database.barcode

import info.armado.ausleihe.database.dataobjects.Prefix

import scala.beans.BeanProperty

/**
  * Factory for [[Barcode]] instances.
  */
object Barcode {
  /**
    * Creates a new Barcode instance from a given barcode string
    *
    * @param str The barcode string
    * @return The new Barcode instance
    */
  def apply(str: String): Barcode = new Barcode(str)

  /**
    * Calculates the checksum for a given barcode string.
    * Beware, that this method does no barcode string validation before.
    *
    * @param str The barcode string
    * @return The calculated checksum
    */
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
      prz += r3 % 10 + r3 / 10
      r3 = 2 * a4
      prz += r3 % 10 + r3 / 10
      r3 = 2 * a6
      prz += r3 % 10 + r3 / 10

      prz = prz % 10

      // negative checksum => bad
      if (prz < 0) {
        prz = prz * (-1)
      }

      prz.toString
    }
  }

  /**
    * Calculates the checksum for a given barcode string made up of a prefix and a counter string
    *
    * @param prefix  The prefix of the barcode
    * @param counter The counter part of the barcode
    * @return The calculated checksum
    */
  def calculateChecksum(prefix: Prefix, counter: String): String = calculateChecksum(prefix.prefix + counter)

  /**
    * Checks if the given barcode string is a valid barcode
    *
    * @param str The barcode string
    * @return True if the barcode string is valid, false otherwise
    */
  def valid(str: String): Boolean = Option(str) match {
    case None | Some("") => false
    case Some(x) if x.length() != 8 => false
    case Some(x) => new Barcode(x) match {
      case Barcode(Prefix.Any | Prefix.Unknown, counter, checksum) => false
      case barcode => barcode.valid
    }
  }

  /**
    * Creates the counter string of a barcode by prepending zeros to the counter number, so that the resulting counter string has a length of numberOfDigits
    *
    * @param counter        The counter number
    * @param numberOfDigits The desired length of the counter string
    * @return The created counter part
    */
  def fill(counter: Int, numberOfDigits: Int): String = {
    var result = counter.toString

    while (result.length() < numberOfDigits) result = "0" + result

    result
  }

  /**
    * Creates a wildcard barcode
    */
  def createWildcard: Barcode = new Barcode(Prefix.Any, "_____", "_")

  /**
    * Creates a wildcard barcode with a given prefix
    *
    * @param prefix The prefix of the to be created wildcard barcode
    */
  def createWildcard(prefix: Prefix) = new Barcode(prefix, "_____", "_")
}

/**
  * A barcode, which can be used on identity cards, envelopes and games
  *
  * @constructor Creates a new Barcode instance with a given barcode prefix, counter and checksum
  * @param prefix   The prefix of the barcode
  * @param counter  The counter part of the barcode
  * @param checksum The checksum of the barcode
  */
case class Barcode(@BeanProperty val prefix: Prefix, @BeanProperty val counter: String, @BeanProperty val checksum: String) {
  /**
    * Creates a new Barcode instance with a prefix string, a counter and a checksum
    *
    * @param prefix   The prefix string of the barcode
    * @param counter  The counter of the barcode
    * @param checksum The checksum of the barcode
    */
  def this(prefix: String, counter: String, checksum: String) = this(Prefix(prefix), counter, checksum)

  /**
    * Creates a new Barcode instance from a given barcode string.
    * This string should have a length of 8 characters.
    * The first two characters are taken as the prefix, the third to seventh characters
    * are taken as the counter and the eight character is taken as the checksum of the barcode.
    *
    * @param barcode The barcode string
    */
  def this(barcode: String) = this(barcode.substring(0, 2), barcode.substring(2, 7), barcode.substring(7, 8))

  /**
    * Creates a new Barcode instance with a given prefix and counter.
    * This method calculates the checksum based on the given prefix and counter itself.
    *
    * @param prefix  The prefix of the barcode
    * @param counter The counter of the barcode
    */
  def this(prefix: Prefix, counter: String) = this(prefix, counter, Barcode.calculateChecksum(prefix, counter))

  /**
    * Creates a new Barcode instance with a given prefix and counter.
    * This method calculates the checksum based on the given prefix and counter itself.
    *
    * @param prefix  The prefix of the barcode
    * @param counter The counter of the barcode
    */
  def this(prefix: Prefix, counter: Int) = this(prefix, Barcode.fill(counter, 5))

  /**
    * Checks if this Barcode instance is valid.
    * The Barcode instance is valid if the checksum of this barcode equals the calculated checksum based on the prefix and counter
    */
  def valid: Boolean = Barcode.calculateChecksum(prefix, counter) match {
    case `checksum` => true
    case _ => false
  }

  override def toString: String = prefix.prefix + counter + checksum
}