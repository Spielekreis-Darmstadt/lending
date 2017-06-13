package info.armado.ausleihe.faces.beans.games;

import info.armado.ausleihe.database.access.GamesDAO;
import info.armado.ausleihe.database.dataobjects.Game;

import java.io.Serializable;
import java.util.Optional;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import lombok.Getter;
import lombok.Setter;

@ViewScoped
@Named("activateGame")
public class ActivateGameBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private GamesDAO gamesDao;
	
	@Getter
	@Setter
	private Game game;
	
	@Getter
	@Setter
	private Optional<Game> lastScannedGame = Optional.empty();
	
	@Transactional
	public void activateGame(boolean resetView) {	
		game.setAvailable(true);		
		gamesDao.update(game);
		
		if (resetView) 	lastScannedGame = Optional.empty();
		else 			lastScannedGame = Optional.of(game);
		
		// reset the input field
		this.game = null;
	}
}
