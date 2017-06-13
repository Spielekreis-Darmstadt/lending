package info.armado.ausleihe.faces.converter;

import info.armado.ausleihe.database.dataobjects.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

@Named("barcodeConverter")
@ApplicationScoped
public class BarcodeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (!Barcode$.MODULE$.valid(value)) {
			FacesMessage msg = new FacesMessage("Barcode Fehler",
					"Der eingegebene Barcode ist nicht valide.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			
			throw new ConverterException(msg);
		}
		
		return new Barcode(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		// barcode.toString()
		return value.toString();
	}

}
