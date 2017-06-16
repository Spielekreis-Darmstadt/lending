/**
 * 
 */
package info.armado.ausleihe.faces.converter;

import info.armado.ausleihe.database.access.IdentityCardDAO;
import info.armado.ausleihe.database.access.LendIdentityCardDAO;
import info.armado.ausleihe.database.barcode.Barcode;
import info.armado.ausleihe.database.barcode.Barcode$;
import info.armado.ausleihe.database.entities.IdentityCard;
import info.armado.ausleihe.database.entities.LendIdentityCard;

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
@Named("lendIdentityCardConverter")
public class LendIdentityCardConverter implements Converter {

	@Inject
	private LendIdentityCardDAO lendIdentityCardDao;
	
	@Inject
	private IdentityCardDAO identityCardDao;
	
	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
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
		
		IdentityCard idCard = identityCardDao.selectByBarcode(new Barcode(value));
		// An idcard with the given barcode doesn't exist
		if (idCard == null || !idCard.getAvailable()) {
			FacesMessage msg = new FacesMessage("Ausweis Fehler",
					"Ein Ausweis mit dem eingegebenen Barcode existiert nicht.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			
			throw new ConverterException(msg);
		}
		
		return lendIdentityCardDao.selectCurrentByIdentityCard(idCard);
	}

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return ((LendIdentityCard)value).getIdentityCard().getBarcode().toString();
	}

}
