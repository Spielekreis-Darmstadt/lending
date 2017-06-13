package info.armado.ausleihe.database.converter;

import java.time.Year;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class YearConverter implements AttributeConverter<Year, Integer> {

	public Integer convertToDatabaseColumn(Year entityValue) {
		if (entityValue == null)
			return null;

		return entityValue.getValue();
	}

	public Year convertToEntityAttribute(Integer databaseValue) {
		if (databaseValue == null)
			return null;

		return Year.parse(String.valueOf(databaseValue));
	}
}
