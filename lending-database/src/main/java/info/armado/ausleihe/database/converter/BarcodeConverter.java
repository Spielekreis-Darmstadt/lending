/**
 * 
 */
package info.armado.ausleihe.database.converter;

import info.armado.ausleihe.database.barcode.Barcode;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Arndt
 *
 */
@Converter(autoApply=true)
public class BarcodeConverter implements AttributeConverter<Barcode, String> {

	@Override
	public String convertToDatabaseColumn(Barcode attribute) {
		return attribute.toString();
	}

	@Override
	public Barcode convertToEntityAttribute(String dbData) {
		return new Barcode(dbData);
	}

}
