/**
 * 
 */
package info.armado.ausleihe.faces.beans.games;

import info.armado.ausleihe.database.access.GamesDAO;
import info.armado.ausleihe.database.dataobjects.Game;
import info.armado.ausleihe.database.objects.GameInfo;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Arndt
 *
 */
@Named("showGameInfos")
@ViewScoped
public class ShowGameInfosBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private GamesDAO gamesDAO;
	
	@Getter
	private List<GameInfo> gameInfos;
	
	@Getter @Setter
	private GameInfo selectedGameInfo;
	
	@Getter @Setter
	private boolean selectOnlyAvailable;
	
	@PostConstruct
	public void init() {
		this.selectOnlyAvailable = true;
		this.gameInfos = gamesDAO.selectAllDifferentGames(selectOnlyAvailable);
	}
	
	public void refreshGameInfos() {
		this.gameInfos = gamesDAO.selectAllDifferentGames(selectOnlyAvailable);
	}
    
    public List<Game> getGames() {
    	return gamesDAO.selectAllGamesWithTitle(selectedGameInfo.getTitle())
    			.stream().filter(game -> game.getAvailable() || !selectOnlyAvailable).collect(Collectors.toList());
    }
}
