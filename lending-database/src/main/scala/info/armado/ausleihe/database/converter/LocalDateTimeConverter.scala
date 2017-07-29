package info.armado.ausleihe.database.converter

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.persistence.{AttributeConverter, Converter}

/**
  * A converter used to convert [[LocalDateTime]] instances to [[Timestamp]] instances, which can be persisted inside a database via JPA.
  *
  * @author Marc Arndt
  * @since 24.06.17
  */
@Converter(autoApply = true)
class LocalDateTimeConverter extends AttributeConverter[LocalDateTime, Timestamp] {
  override def convertToEntityAttribute(databaseValue: Timestamp): LocalDateTime =
    Option(databaseValue).map(_.toLocalDateTime).orNull

  override def convertToDatabaseColumn(entityValue: LocalDateTime): Timestamp =
    Option(entityValue).map(Timestamp.valueOf(_)).orNull
}
