/**
 * 
 */
package info.armado.ausleihe.faces.beans;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * @author marc
 *
 */
public abstract class AdderInterface {
	
	public static final String notNeeded = "Verwerfen";
	
	protected String idFieldName;
	
	public AdderInterface(String idFieldName) {
		this.idFieldName = idFieldName;
	}
	
	public void processData(XLSHandler xlsHandler, Map<String, Integer> felder) {
		xlsHandler.validateIDs(felder.get(idFieldName));
		
		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getMessageList("mappingMessages").isEmpty())
			addItems(xlsHandler.getRows(), felder);
	}
	
	public abstract String getDefaultDatabaseFieldName();
	
	public abstract String[] getDatabaseFieldNames();
	
	public abstract void addItems(List<XLSRow> rows, Map<String, Integer> felder);
	
	/**
	 * This method creates a new java server faces error message with the given error message text.
	 * The newly created error message is then added to the mappingMessages object on the jsf GUI 
	 * @param messageText The error message text
	 */
	protected void addFacesErrorMessage(String messageText) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL,
				messageText, "");
		context.addMessage("mappingMessages", message);
	}
}
