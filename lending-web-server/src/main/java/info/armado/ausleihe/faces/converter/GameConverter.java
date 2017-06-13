/**
 * 
 */
package info.armado.ausleihe.faces.converter;

import info.armado.ausleihe.database.access.GamesDAO;
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
@Named("gameConverter")
public class GameConverter implements Converter {

	@Inject
	private GamesDAO gamesDao; 
	
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
		
		Game game = gamesDao.selectByBarcode(new Barcode(value));
		
		// A game with the given barcode doesn't exists
		if (game == null) {
			FacesMessage msg = new FacesMessage("Spiel Fehler",
					"Ein Spiel mit dem eingegebenen Barcode existiert nicht.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			
			throw new ConverterException(msg);
		}
		
		return game;
	}

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return ((Game)value).getBarcode().toString();
	}

}
