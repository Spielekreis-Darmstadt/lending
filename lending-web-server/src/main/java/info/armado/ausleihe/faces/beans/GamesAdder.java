/**
 * 
 */
package info.armado.ausleihe.faces.beans;

import info.armado.ausleihe.database.access.GamesDAO;
import info.armado.ausleihe.database.dataobjects.*;
import info.armado.ausleihe.database.barcode.Barcode;
import info.armado.ausleihe.database.barcode.Barcode$;
import info.armado.ausleihe.database.entities.Game;
import info.armado.ausleihe.database.enums.Prefix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

/**
 * @author marc
 *
 */
public class GamesAdder extends AdderInterface {
	@Getter
	private Prefix prefix;

	private GamesDAO gamesDao;

	private boolean activateGames;

	public GamesAdder(Prefix prefix, GamesDAO gamesDao, boolean activateGames) {
		super("SID");

		this.prefix = prefix;
		this.gamesDao = gamesDao;
		this.activateGames = activateGames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * info.armado.ausleihe.faces.beans.AdderInterface#getDatabaseFieldNames()
	 */
	@Override
	public String[] getDatabaseFieldNames() {
		return new String[] { idFieldName, "Titel", "Autor", "Verlag", "Mindestspielerzahl", "Maximalspielerzahl",
				"Kombiniertespielerzahl", "Mindestalter", "Spieldauer", notNeeded };
	}
	
	@Override
	public String getDefaultDatabaseFieldName() {
		return notNeeded;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * info.armado.ausleihe.faces.beans.AdderInterface#addItems(java.util.List,
	 * java.util.Map)
	 */
	@Override
	public void addItems(List<XLSRow> rows, Map<String, Integer> felder) {
		if (!felder.containsKey(idFieldName) || !felder.containsKey("Titel"))
			return;

		List<Game> games = new ArrayList<Game>(rows.size());

		// create the Game objects
		for (XLSRow row : rows) {
			Game game = new Game();
			// set the sid
			String id = row.getValue(felder.get(idFieldName));
			if (Barcode$.MODULE$.valid(id))
				game.setBarcode(new Barcode(id));
			else
				game.setBarcode(new Barcode(prefix, Integer.parseInt(id)));
			// set the title
			if (felder.containsKey("Titel"))
				game.setTitle(row.getValue(felder.get("Titel")));
			// set the author
			if (felder.containsKey("Autor"))
				game.setAuthor(row.getValue(felder.get("Autor")));
			// set the publisher
			if (felder.containsKey("Verlag"))
				game.setPublisher(row.getValue(felder.get("Verlag")));

			if (felder.containsKey("Mindestspielerzahl") && felder.containsKey("Maximalspielerzahl")) {
				String minPlayerCount = row.getValue(felder.get("Mindestspielerzahl"));
				String maxPlayerCount = row.getValue(felder.get("Maximalspielerzahl"));

				if (isInteger(minPlayerCount) && isInteger(maxPlayerCount))
					game.setPlayerCount(
							new PlayerCount(Integer.parseInt(minPlayerCount), Integer.parseInt(maxPlayerCount)));
			}

			if (felder.containsKey("Kombiniertespielerzahl")) {
				String playerCountField = row.getValue(felder.get("Kombiniertespielerzahl"));

				if (playerCountField != null) {
					String[] counts = playerCountField.split(" ?- ?");

					if (counts.length == 2 && isInteger(counts[0]) && isInteger(counts[1]))
						game.setPlayerCount(new PlayerCount(Integer.parseInt(counts[0]), Integer.parseInt(counts[1])));
					else if (counts.length == 1 && isInteger(counts[0]))
						game.setPlayerCount(new PlayerCount(Integer.parseInt(counts[0])));
				}
			}

			if (felder.containsKey("Mindestalter")) {
				String minimumAge = row.getValue(felder.get("Mindestalter"));

				if (isInteger(minimumAge))
					game.setMinimumAge(Integer.parseInt(minimumAge));
			}

			if (felder.containsKey("Spieldauer")) {
				String durationValue = row.getValue(felder.get("Spieldauer"));

				if (durationValue != null) {
					String[] dauern = durationValue.split(" ?- ?");
					if (dauern.length == 2 && isInteger(dauern[0]) && isInteger(dauern[1]))
						game.setGameDuration(
								new GameDuration(Integer.parseInt(dauern[0]), Integer.parseInt(dauern[1])));
					else if (dauern.length == 1 && isInteger(dauern[0]))
						game.setGameDuration(new GameDuration(Integer.parseInt(dauern[0])));
				}
			}

			game.setAvailable(activateGames);

			games.add(game);
		}

		Set<Barcode> alreadyPersistedBarcodes = gamesDao.selectAllGamesAlreadyPersisted(games);

		if (alreadyPersistedBarcodes.isEmpty())
			gamesDao.insert(games);
		else {
			for (Barcode barcode : alreadyPersistedBarcodes) {
				this.addFacesErrorMessage(
						String.format("Barcode ist bereits in der Datenbank enthalten: \"%s\"", barcode.toString()));
			}
		}
	}

	private boolean isInteger(String string) {
		if (string == null)
			return false;
		
		return string.matches("^\\d+$");
	}
}
