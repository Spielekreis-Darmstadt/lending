package info.armado.ausleihe.client.model

import info.armado.ausleihe.remote.dataobjects.entities.IdentityCardData
import info.armado.ausleihe.remote.dataobjects.entities.EnvelopeData
import info.armado.ausleihe.remote.dataobjects.entities.GameData

object BarcodeTest {
  def apply(text: String): BarcodeTest = text match {
    case _ if text.length() != 8 => new WrongLength(text)
    case _ if !BarcodeChars.getInstance.isBarcodeValid(text) => new WrongChecksum(text)
    case _ => new Barcode(text.substring(0, 2), text.substring(2, 7), text.substring(7))
  }
}

sealed class BarcodeTest
case class Barcode(prefix: String, counter: String, checksum: String) extends BarcodeTest
case class WrongLength(input: String) extends BarcodeTest
case class WrongChecksum(input: String) extends BarcodeTest