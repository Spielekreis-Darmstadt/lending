/**
 * 
 */
package info.armado.ausleihe.faces.converter;

import info.armado.ausleihe.database.access.IdentityCardDAO;
import info.armado.ausleihe.database.dataobjects.*;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author marc
 *
 */
@Named("identityCardConverter")
public class IdentityCardConverter implements Converter {

	@Inject
	private IdentityCardDAO idCardDao;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		// The given string is not a valid barcode
		if (!Barcode$.MODULE$.valid(value)) {
			FacesMessage msg = new FacesMessage("Barcode Fehler",
					"Der eingegebene Barcode ist nicht valide.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			
			throw new ConverterException(msg);
		}
		
		IdentityCard idCard = idCardDao.selectByBarcode(new Barcode(value));
		
		// An idcard with the given barcode doesn't exist
		if (idCard == null) {
			FacesMessage msg = new FacesMessage("Ausweis Fehler",
					"Ein Ausweis mit dem eingegebenen Barcode existiert nicht.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			
			throw new ConverterException(msg);
		}
		
		return idCard;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return ((IdentityCard)value).getBarcode().toString();
	}

}
