/**
 * 
 */
package info.armado.ausleihe.faces.beans.games;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import info.armado.ausleihe.database.access.GamesDAO;
import info.armado.ausleihe.database.entities.Game;
import info.armado.ausleihe.database.dataobjects.GameDuration;
import info.armado.ausleihe.database.dataobjects.PlayerCount;
import lombok.Getter;

/**
 * @author Arndt
 *
 */
@ViewScoped
@Named("addGame")
public class AddGameBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private GamesDAO gameDao;

	@Getter
	private Game newGame;

	@PostConstruct
	public void init() {
		this.newGame = new Game();
		this.newGame.setPlayerCount(new PlayerCount());
		this.newGame.setGameDuration(new GameDuration());
	}

	@Transactional
	public void save(boolean resetEnabled) {
		if (gameDao.exists(newGame.getBarcode())) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Existierender Barcode",
									"Ein Spiel mit dem eingegebenen Barcode existiert bereits."));
		} else {
			gameDao.insert(newGame);

			if (resetEnabled) {
				this.newGame = new Game();
				this.newGame.setPlayerCount(new PlayerCount());
				this.newGame.setGameDuration(new GameDuration());
			} else 
				this.newGame = newGame.createOnBasis();
		}
	}
}
