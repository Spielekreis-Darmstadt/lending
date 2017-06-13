/**
 * 
 */
package info.armado.ausleihe.faces.beans.statistics;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
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
@Named("showLendIdentityCards")
@RequestScoped
public class ShowLendIdentityCardsBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private LendIdentityCardDAO lendIdentityCardDao;
	
	@Getter
	private List<LendIdentityCard> lendIdentityCards;
	
	@Getter @Setter
	private List<LendIdentityCard> filteredLendIdentityCards;
	
	@PostConstruct
	public void init() {
		this.lendIdentityCards = lendIdentityCardDao.selectAllCurrentlyLend();
	}
}
