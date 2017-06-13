/**
 * 
 */
package info.armado.ausleihe.faces.beans.games;

import info.armado.ausleihe.database.access.GamesDAO;
import info.armado.ausleihe.database.access.LendGameDAO;
import info.armado.ausleihe.database.dataobjects.Game;

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
@Named("deactivateGame")
@ViewScoped
public class DeactivateGameBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private GamesDAO gamesDao;

	@Inject
	private LendGameDAO lendGameDao;

	@Getter
	@Setter
	private Game selectedGame;

	private List<Game> games;

	@PostConstruct
	public void init() {
		this.games = gamesDao.selectAllAvailableGames();
	}

	public List<Game> completeGame(String query) {
		return games.stream().filter(game -> game.getBarcode().toString().startsWith(query))
				.collect(Collectors.toList());
	}

	public void save() {
		FacesMessage message = null;
		if (!lendGameDao.isGameLend(selectedGame)) {
			selectedGame.setAvailable(false);
			gamesDao.update(selectedGame);

			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Spiel deaktiviert", String
					.format("Das Spiel \"%s\" wurde erfolgreich deaktiviert.", selectedGame.getBarcode().toString()));

			// Inputreset
			selectedGame = null;
		} else {
			// Show a message to inform that the game is currently lend
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Spiel ist verliehen",
					String.format("Das Spiel \"%s\" ist zur Zeit verliehen.", selectedGame.getBarcode().toString()));
		}
		
		FacesContext.getCurrentInstance().addMessage("messages", message);
	}
}
