package info.armado.ausleihe.database.converter

import javax.persistence.{AttributeConverter, Converter}

import info.armado.ausleihe.database.barcode.Barcode

/**
  * A converter used to convert [[Barcode]] instances to [[String]]s to be able to persist them inside a database.
  *
  * @author Marc Arndt
  * @since 24.06.17
  */
@Converter(autoApply = true)
class BarcodeConverter extends AttributeConverter[Barcode, String] {
  override def convertToEntityAttribute(databaseValue: String): Barcode = Barcode(databaseValue)

  override def convertToDatabaseColumn(entityValue: Barcode): String = entityValue.toString
}
