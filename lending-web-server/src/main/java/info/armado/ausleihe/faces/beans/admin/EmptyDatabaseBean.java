/**
 * 
 */
package info.armado.ausleihe.faces.beans.admin;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import info.armado.ausleihe.database.access.EnvelopeDAO;
import info.armado.ausleihe.database.access.GamesDAO;
import info.armado.ausleihe.database.access.IdentityCardDAO;
import info.armado.ausleihe.database.access.LendGameDAO;
import info.armado.ausleihe.database.access.LendIdentityCardDAO;
import info.armado.ausleihe.database.enums.Prefix;
import lombok.Getter;
import lombok.Setter;

/**
 * @author marc
 *
 */
@Named("emptyDatabase")
@RequestScoped
public class EmptyDatabaseBean {
	
	@Inject
	private GamesDAO gamesDao;
	
	@Inject
	private IdentityCardDAO identityCardDao;
	
	@Inject
	private EnvelopeDAO envelopeDao;
	
	@Inject
	private LendGameDAO lendGameDao;
	
	@Inject
	private LendIdentityCardDAO lendIdentityCardDao;
	
	@Getter @Setter
	private boolean deleteLendGames;
	
	@Getter @Setter
	private boolean deleteLendIdentityCards;
	
	@Getter @Setter
	private boolean deleteSpielekreisGames;
	
	@Getter @Setter
	private boolean deleteBDKJGames;
	
	@Getter @Setter
	private boolean deleteIdentityCards;
	
	@Getter @Setter
	private boolean deleteEnvelopes;
	
	public void emptyTables() {
		if (deleteLendGames) {
			lendGameDao.deleteAll();
		}
		
		if (deleteLendIdentityCards) {
			lendIdentityCardDao.deleteAll();
		}
		
		if (deleteSpielekreisGames) {
			gamesDao.deleteAllGamesFrom(Prefix.Spielekreis);
		}
		
		if (deleteBDKJGames) {
			gamesDao.deleteAllGamesFrom(Prefix.BDKJ);
		}
		
		if (deleteIdentityCards) {
			identityCardDao.deleteAll();
		}
		
		if (deleteEnvelopes) {
			envelopeDao.deleteAll();
		}
		
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Datenbank zurückgesetzt.", "Die ausgewählten Tabellen wurden geleert.");
        FacesContext.getCurrentInstance().addMessage("messages", message);
	}
}
