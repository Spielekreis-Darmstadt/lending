/**
 * 
 */
package info.armado.ausleihe.client.view.javafx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import info.armado.ausleihe.client.graphics.screen.util.FocusRequestable;
import info.armado.ausleihe.client.graphics.screen.util.Resetable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * @author marc
 *
 */
public class AusleiheScreen extends AnchorPane {
	@FXML
	private TabPane tabpane;

	@FXML
	private Tab issueGames;
	@FXML
	private Tab unlimitedIssueGames;
	@FXML
	private Tab returnGames;
	@FXML
	private Tab issueIdCards;
	@FXML
	private Tab returnIdCards;
	@FXML
	private Tab barcodeTesting;
	@FXML
	private Tab gameSearch;
	
	private Map<KeyCode, Tab> keyMap;

	public AusleiheScreen() {
		super();

		this.keyMap = new HashMap<>();

		this.loadFxml();
	}
	
	@FXML
	public void initialize() {
		this.associateKeys();

		this.addEventFilter(KeyEvent.KEY_RELEASED, this::handleTabChanges);
		this.tabpane.getSelectionModel().selectedItemProperty().addListener((observableTab, oldTab, newTab) -> {
			Platform.runLater(() -> {
				((Resetable) newTab.getContent()).reset();
				((FocusRequestable) newTab.getContent()).askForFocus();
			});
		});
		
		this.selectTab(issueGames);
		
		Platform.runLater(() -> {
			this.resetCurrentTab();
			this.focusCurrentTab();
		});
	}

	private void loadFxml() {
		FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getClassLoader().getResource("javafx/overview.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	private void associateKeys() {
		this.keyMap.put(KeyCode.F1, issueGames);
		this.keyMap.put(KeyCode.F2, unlimitedIssueGames);
		this.keyMap.put(KeyCode.F3, returnGames);
		this.keyMap.put(KeyCode.F4, issueIdCards);
		this.keyMap.put(KeyCode.F5, returnIdCards);
		this.keyMap.put(KeyCode.F6, barcodeTesting);
		this.keyMap.put(KeyCode.F7, gameSearch);
	}

	public void selectTab(Tab tab) {
		tabpane.getSelectionModel().select(tab);
	}

	public Tab getCurrentTab() {
		return tabpane.getSelectionModel().getSelectedItem();
	}

	public void focusCurrentTab() {
		((FocusRequestable) getCurrentTab().getContent()).askForFocus();
	}

	public void resetCurrentTab() {
		((Resetable) getCurrentTab().getContent()).reset();
	}

	public void handleTabChanges(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE && event.isControlDown()) {
			Platform.exit();
			System.exit(0);
		} else {
			if (keyMap.containsKey(event.getCode())) {
				selectTab(keyMap.get(event.getCode()));
			} else if (event.getCode() == KeyCode.ESCAPE) {
				resetCurrentTab();
				focusCurrentTab();
			}
		}
	}
}
