/**
 * 
 */
package info.armado.ausleihe.faces.beans.games;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import info.armado.ausleihe.database.access.GamesDAO;
import info.armado.ausleihe.database.entities.Game;
import info.armado.ausleihe.database.enums.Prefix;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Arndt
 *
 */
@Named("showGames")
@ViewScoped
public class ShowGamesBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private GamesDAO gamesDao;

	@Getter
	private List<Game> games;

	@Getter
	@Setter
	private List<Game> filteredGames;

	public Prefix[] getOrganisators() {
		return new Prefix[] { Prefix.Spielekreis, Prefix.BDKJ };
	}

	@PostConstruct
	public void init() {
		this.games = gamesDao.selectAll();
	}

}
