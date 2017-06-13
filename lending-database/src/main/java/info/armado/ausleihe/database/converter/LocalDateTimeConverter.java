package info.armado.ausleihe.database.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
	
    public java.sql.Timestamp convertToDatabaseColumn(LocalDateTime entityValue) {
    	if (entityValue == null)
    		return null;
    	
        return Timestamp.valueOf(entityValue);
    }

    public LocalDateTime convertToEntityAttribute(java.sql.Timestamp databaseValue) {
    	if (databaseValue == null)
    		return null;
    	
        return databaseValue.toLocalDateTime();
    }
}
