/**
 * 
 */
package info.armado.ausleihe.faces.converter;

import info.armado.ausleihe.database.access.EnvelopeDAO;
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
@Named("envelopeConverter")
public class EnvelopeConverter implements Converter {

	@Inject
	private EnvelopeDAO envelopeDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent, java.lang.String)
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
		
		Envelope envelope = envelopeDao.selectByBarcode(new Barcode(value));
		
		// An envelope with the given barcode doesn't exist
		if (envelope == null) {
			FacesMessage msg = new FacesMessage("Umschlag Fehler",
					"Ein Umschlag mit dem eingegebenen Barcode existiert nicht.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			
			throw new ConverterException(msg);
		}

		return envelope;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return ((Envelope) value).getBarcode().toString();
	}

}
