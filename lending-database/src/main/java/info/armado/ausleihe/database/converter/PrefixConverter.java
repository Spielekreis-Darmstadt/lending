/**
 * 
 */
package info.armado.ausleihe.database.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import info.armado.ausleihe.database.enums.Prefix;

/**
 * @author marc
 *
 */
@Converter(autoApply = true)
public class PrefixConverter implements AttributeConverter<Prefix, String> {

	@Override
	public String convertToDatabaseColumn(Prefix attribute) {
		return attribute.getPrefix();
	}

	@Override
	public Prefix convertToEntityAttribute(String dbData) {
		return Prefix.parse(dbData);
	}

}
