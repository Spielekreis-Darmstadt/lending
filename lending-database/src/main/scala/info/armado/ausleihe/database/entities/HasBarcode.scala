package info.armado.ausleihe.database.entities

import info.armado.ausleihe.database.barcode.Barcode

trait HasBarcode {
  var barcode: Barcode
}
