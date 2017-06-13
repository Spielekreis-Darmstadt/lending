/**
 * 
 */
package info.armado.ausleihe.faces.beans.identitycards;

import info.armado.ausleihe.database.access.IdentityCardDAO;
import info.armado.ausleihe.database.access.LendIdentityCardDAO;
import info.armado.ausleihe.database.dataobjects.IdentityCard;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

/**
 * @author marc
 *
 */
@Named("deactivateIdentityCard")
@ViewScoped
public class DeactivateIdentityCardBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private IdentityCardDAO identityCardDao;

	@Inject
	private LendIdentityCardDAO lendIdentityCardDao;

	@Getter
	@Setter
	private IdentityCard selectedIdentityCard;

	@Getter
	@Setter
	private List<IdentityCard> identityCards;

	@PostConstruct
	public void init() {
		this.identityCards = identityCardDao.selectAllAvailable();
	}

	public List<IdentityCard> completeIdentityCard(String query) {
		return identityCards.stream().filter(idCard -> idCard.getBarcode().toString().startsWith(query))
				.collect(Collectors.toList());
	}

	public void save() {
		FacesMessage message = null;
		if (!lendIdentityCardDao.isIdentityCardIssued(selectedIdentityCard)) {
			selectedIdentityCard.setAvailable(false);
			identityCardDao.update(selectedIdentityCard);
			
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Ausweis deaktiviert", String.format(
					"Der Ausweis \"%s\" wurde erfolgreich deaktiviert.", selectedIdentityCard.getBarcode().toString()));
			// Inputreset
			selectedIdentityCard = null;
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ausweis ist ausgegeben", String.format(
					"Der Ausweis \"%s\" ist zur Zeit ausgegeben.", selectedIdentityCard.getBarcode().toString()));
		}
		FacesContext.getCurrentInstance().addMessage("messages", message);
	}
}
