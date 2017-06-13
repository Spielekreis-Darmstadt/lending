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

import info.armado.ausleihe.database.access.LendGameDAO;
import info.armado.ausleihe.database.dataobjects.LendGame;
import lombok.Getter;
import lombok.Setter;

/**
 * @author marc
 *
 */
@Named("showLendGames")
@RequestScoped
public class ShowLendGamesBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private LendGameDAO lendGameDao;
	
	@Getter
	private List<LendGame> lendGames;
	
	@Getter @Setter
	private List<LendGame> filteredLendGames;
	
	@PostConstruct
	public void init() {
		this.lendGames = lendGameDao.selectAllCurrentLendGames();
	}
}
