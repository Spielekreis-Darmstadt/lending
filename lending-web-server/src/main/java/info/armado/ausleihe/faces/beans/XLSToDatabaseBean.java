/**
 * 
 */
package info.armado.ausleihe.faces.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.primefaces.model.UploadedFile;

import info.armado.ausleihe.database.access.EnvelopeDAO;
import info.armado.ausleihe.database.access.GamesDAO;
import info.armado.ausleihe.database.access.IdentityCardDAO;
import info.armado.ausleihe.database.enums.Prefix;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

/**
 * @author Arndt
 *
 */
@Log
@Named("XLSToDatabase")
@FlowScoped(value = "addGames")
public class XLSToDatabaseBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	@Getter
	private XLSHandler xlsHandler;

	@Inject
	private GamesDAO gamesDao;

	@Inject
	private IdentityCardDAO idCardDao;

	@Inject
	private EnvelopeDAO envelopeDao;

	@Getter
	private Optional<AdderInterface> adder;

	@Getter
	@Setter
	private UploadedFile uploadedFile;

	@Getter
	@Setter
	private XLSType selectedType;

	@Getter
	@Setter
	private boolean availableState;

	@PostConstruct
	public void init() {
		this.selectedType = XLSType.GAMES_SPIELEKREIS;
		this.adder = Optional.empty();
	}

	public XLSType[] getTypes() {
		return XLSType.values();
	}

	public String[] getDatabaseFieldNames() {
		return adder.map(add -> add.getDatabaseFieldNames()).orElse(new String[0]);
	}

	public String selectFile() throws IOException, InvalidFormatException {
		log.log(Level.INFO, String.format("Datei \"%s\" (Größe: %d) hochgeladen", uploadedFile.getFileName(),
				uploadedFile.getSize()));

		// Open the uploaded excel file
		xlsHandler.loadWorkbook(uploadedFile);

		return "addGames2";
	}

	public String selectSheet() {
		log.log(Level.INFO, String.format("Sheet \"%s\" ausgewählt.", xlsHandler.getSelectedSheet()));

		// Load the correct handler for the data
		switch (selectedType) {
		case GAMES_SPIELEKREIS:
			adder = Optional.of(new GamesAdder(Prefix.Spielekreis, gamesDao, availableState));
			break;
		case GAMES_BDKJ:
			adder = Optional.of(new GamesAdder(Prefix.BDKJ, gamesDao, availableState));
			break;
		case IDENTITY_CARDS:
			adder = Optional.of(new IdentityCardsAdder(idCardDao, availableState));
			break;
		case ENVELOPES:
			adder = Optional.of(new EnvelopesAdder(envelopeDao, availableState));
			break;
		default:
			adder = Optional.empty();
			return null;
		}

		if (adder.isPresent()) {
			// Open the selected Sheet
			xlsHandler.selectSheet(adder.get());

			return "addGames3";
		} else {
			return null;
		}
	}

	public String writeToDatabase() {
		HashMap<String, Integer> felder = this.createDatabaseToXLSColumnMapping(xlsHandler.getColumnHeaders());

		adder.ifPresent(add -> add.processData(xlsHandler, felder));

		FacesContext context = FacesContext.getCurrentInstance();
		if (context.getMessageList("mappingMessages").isEmpty())
			return "returnAddGames";
		else
			return null;
	}

	/**
	 * This method creates a new java server faces error message with the given
	 * error message text. The newly created error message is then added to the
	 * mappingMessages object on the jsf GUI
	 * 
	 * @param messageText
	 *            The error message text
	 */
	private void addFacesErrorMessage(String messageText) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, messageText, "");
		context.addMessage("mappingMessages", message);
	}

	/**
	 * This method creates a mapping between the database column names and xls
	 * column ids as a HashMap. To do this it takes a XLSPair array and
	 * transforms its content to a HashMap The resulting HashMap from this
	 * method maps from a database column name to the corresponding column id in
	 * the xls file
	 * 
	 * @param mappings
	 *            The mapping to be transformed into a HashMap
	 * @return The created HashMap in the form database column name -> xls
	 *         column id
	 */
	private HashMap<String, Integer> createDatabaseToXLSColumnMapping(Map<Integer, XLSColumn> mappings) {
		// mapping from database column name to xls column id
		HashMap<String, Integer> result = new HashMap<String, Integer>();

		for (XLSColumn pair : mappings.values()) {
			// if the column is to be ignored (not needed)
			if (pair.getDatabaseName().equals(AdderInterface.notNeeded))
				continue;
			// if the database column is not set, or appearing multiple times
			if (pair.getDatabaseName() == null || result.containsKey(pair.getDatabaseName())) {
				this.addFacesErrorMessage(String.format("Doppelte Datenbank Spalte: \"%s\"", pair.getDatabaseName()));
			}
			result.put(pair.getDatabaseName(), pair.getXlsColumn());
		}

		return result;
	}

	public enum XLSType {
		GAMES_SPIELEKREIS("Spiele Spielekreis", "Hinzufügen von Spielen für den Spielekreis Darmstadt"), GAMES_BDKJ(
				"Spiele BDKJ", "Hinzufügen von Spielen für den BDKJ"), IDENTITY_CARDS("Ausweise",
						"Hinzufügen von Ausweisen"), ENVELOPES("Umschläge", "Hinzufügen von Umschlägen");

		@Getter
		private String label;

		@Getter
		private String tooltip;

		XLSType(String label, String tooltip) {
			this.label = label;
			this.tooltip = tooltip;
		}
	}
}
