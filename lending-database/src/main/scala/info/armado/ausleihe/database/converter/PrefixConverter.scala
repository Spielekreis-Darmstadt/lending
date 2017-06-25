package info.armado.ausleihe.database.converter

import javax.persistence.{AttributeConverter, Converter}

import info.armado.ausleihe.database.enums.Prefix

/**
  * A converter used to convert [[Prefix]] instances to [[String]]s, which can be persisted inside a database via JPA.
  *
  * @author Marc Arndt
  * @since 24.06.17
  */
@Converter(autoApply = true)
class PrefixConverter extends AttributeConverter[Prefix, String] {
  override def convertToEntityAttribute(databaseValue: String): Prefix = Prefix.parse(databaseValue)

  override def convertToDatabaseColumn(entityValue: Prefix): String = entityValue.getPrefix
}
