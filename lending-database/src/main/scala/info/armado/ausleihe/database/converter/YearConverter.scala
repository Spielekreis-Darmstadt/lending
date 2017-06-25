package info.armado.ausleihe.database.converter

import java.time.Year
import javax.persistence.{AttributeConverter, Converter}

/**
  * A converter used to convert [[Year]] instances to [[Integer]] values, which can be persisted inside a database via JPA.
  *
  * @author Marc Arndt
  * @since 24.06.17
  */
@Converter(autoApply = true)
class YearConverter extends AttributeConverter[Year, Integer] {
  override def convertToEntityAttribute(databaseValue: Integer): Year =
    Option(databaseValue).map(value => Year.parse(value.toString)).orNull

  override def convertToDatabaseColumn(entityValue: Year): Integer =
    Option(entityValue).map[Integer](_.getValue).orNull
}
