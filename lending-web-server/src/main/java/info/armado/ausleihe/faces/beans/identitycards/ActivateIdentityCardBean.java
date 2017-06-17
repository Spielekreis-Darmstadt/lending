/**
 * 
 */
package info.armado.ausleihe.faces.beans.identitycards;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import info.armado.ausleihe.database.access.IdentityCardDAO;
import info.armado.ausleihe.database.entities.IdentityCard;
import lombok.Getter;
import lombok.Setter;

/**
 * @author marc
 *
 */
@Named("activateIdentityCard")
@ViewScoped
public class ActivateIdentityCardBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private IdentityCardDAO identityCardDao;
	
	@Getter @Setter
	private IdentityCard idCard;
	
	@Transactional
	public void activateIdentityCard() {
		idCard.setAvailable(true);		
		identityCardDao.update(idCard);
		
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Ausweis aktiviert", String.format("Der Ausweis \"%s\" wurde erfolgreich aktiviert.", idCard.getBarcode().toString()));
		FacesContext.getCurrentInstance().addMessage("messages", message);
		
		// Inputreset
		idCard = null;
	}
}
