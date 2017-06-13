/**
 * 
 */
package info.armado.ausleihe.faces.beans.identitycards;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import info.armado.ausleihe.database.access.LendIdentityCardDAO;
import info.armado.ausleihe.database.dataobjects.LendIdentityCard;
import lombok.Getter;
import lombok.Setter;

/**
 * @author marc
 *
 */
@Named("setOwner")
@ViewScoped
public class SetOwnerBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private LendIdentityCardDAO lendIdentityCardDao;
	
	@Getter
	private List<LendIdentityCard> lendIdCards;
	
	@Getter @Setter
	private LendIdentityCard selectedLendIdentityCard;
	
	public boolean isItemSelected() {
		return selectedLendIdentityCard != null;
	}
	
	@PostConstruct
	public void init() {
		this.lendIdCards = lendIdentityCardDao.selectAllCurrentlyLend();
	}
	
	public List<LendIdentityCard> completeIdentityCard(String query) {
		return lendIdCards.stream().filter(idCard -> idCard.getIdentityCard().getBarcode().toString().startsWith(query)).collect(Collectors.toList());
	}
	
	public void save() {			
		lendIdentityCardDao.update(selectedLendIdentityCard);
		
		FacesMessage msg = new FacesMessage("Eigentümer geändert",
				"Der Eigentümer des Ausweises wurde auf \"" + selectedLendIdentityCard.getOwner() + "\" geändert.");
		msg.setSeverity(FacesMessage.SEVERITY_INFO);
		
		FacesContext.getCurrentInstance().addMessage("ownerChangedMessage", msg);
	}
}
