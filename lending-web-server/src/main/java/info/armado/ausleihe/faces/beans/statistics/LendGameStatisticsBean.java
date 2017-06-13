package info.armado.ausleihe.faces.beans.statistics;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import info.armado.ausleihe.database.access.LendGameDAO;
import info.armado.ausleihe.database.enums.Prefix;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Contains the knowledge, which game was lend out how many times on darmstadt spielt.
 * 
 * @author marc
 *
 */
@Named("lendGameStatistics")
@ViewScoped
public class LendGameStatisticsBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private LendGameDAO lendGameDao;

	@Getter
	private List<LendGameWrapper> lendGames;
	
	@Getter @Setter
	private List<LendGameWrapper> filteredLendGames;
	
	@Getter @Setter
	private String organisator;

	@PostConstruct
	private void init() {
		this.organisator = "Alle";
		this.loadLendGames();
	}

	public void loadLendGames() {
		this.lendGames = lendGameDao.selectAll().stream()
				.filter(lendGame -> {
					switch (organisator) {
					case "Alle":
						return true;
					case "Spielekreis":
						return lendGame.getGame().getBarcode().getPrefix().equals(Prefix.Spielekreis);
					case "BDKJ":
						return lendGame.getGame().getBarcode().getPrefix().equals(Prefix.BDKJ);
					default: 
						return true;
					}
				})
				.collect(Collectors.groupingBy(lendGame -> lendGame.getGame().getTitle(), Collectors.counting())).entrySet()
				.stream().map(entry -> new LendGameWrapper(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}
	
	@AllArgsConstructor
	public class LendGameWrapper {
		/**
		 * The title of the lend game
		 */
		@Getter
		private String title;

		/**
		 * The number of times a game with the title was lend
		 */
		@Getter
		private long lendNumber;
	}
}
