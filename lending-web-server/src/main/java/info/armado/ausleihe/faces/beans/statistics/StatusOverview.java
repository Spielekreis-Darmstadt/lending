/**
 * 
 */
package info.armado.ausleihe.faces.beans.statistics;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import info.armado.ausleihe.database.access.GamesDAO;
import info.armado.ausleihe.database.access.IdentityCardDAO;
import info.armado.ausleihe.database.access.LendGameDAO;
import info.armado.ausleihe.database.access.LendIdentityCardDAO;
import lombok.Getter;

/**
 * @author marc
 *
 */
@Named("statusOverview")
@RequestScoped
public class StatusOverview implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private GamesDAO gamesDao;
	
	@Inject
	private LendGameDAO lendGameDao;
	
	@Inject
	private IdentityCardDAO identityCardDao;
	
	@Inject
	private LendIdentityCardDAO lendIdentityCardDao;
	
	@Getter
	private long numberOfAvailableGames;
	
	@Getter
	private long numberOfAvailableBDKJGames;
	
	@Getter
	private long numberOfAvailableSpielekreisGames;
	
	@Getter
	private long numberOfAvailableIdentityCards;
	
	@Getter
	private long numberOfCurrentLendGames;
	
	@Getter
	private long numberOfCurrentLendIdentityCards;
	
	@Getter
	private long numberOfCurrentLendBDKJGames;
	
	@Getter
	private long numberOfCurrentLendSpielekreisGames;
	
	@Getter
	private long numberOfTotalLendGames;
	
	@Getter
	private long numberOfTotalLendIdentityCards;
	
	@Getter
	private BarChartModel barModel;
	
	@PostConstruct
	public void init() {
		this.numberOfAvailableGames = gamesDao.selectNumberOfAvailableGames();
		this.numberOfAvailableBDKJGames = gamesDao.selectNumberOfAvailableBDKJGames();
		this.numberOfAvailableSpielekreisGames = gamesDao.selectNumberOfAvailableSpielekreisGames();
		this.numberOfAvailableIdentityCards = identityCardDao.selectNumberOfAvailableIdentityCards();
		
		this.numberOfCurrentLendGames = lendGameDao.selectNumberOfCurrentLendGames();
		this.numberOfCurrentLendBDKJGames = lendGameDao.selectNumberOfCurrentLendBDKJGames();
		this.numberOfCurrentLendSpielekreisGames = lendGameDao.selectNumberOfCurrentLendSpielekreisGames();
		this.numberOfCurrentLendIdentityCards = lendIdentityCardDao.selectNumberOfCurrentLendIdentityCards();
		
		this.numberOfTotalLendGames = lendGameDao.selectNumberOfTotalLendGames();
		this.numberOfTotalLendIdentityCards = lendIdentityCardDao.selectNumberOfTotalLendIdentityCards();
		
		this.barModel = createBarModel();
	}
	
	public BarChartModel createBarModel() {
		BarChartModel model = new BarChartModel();
		
		ChartSeries lend = new ChartSeries();
		lend.setLabel("Verliehen");
		lend.set("Spiele", numberOfCurrentLendGames);
		lend.set("Ausweise", numberOfCurrentLendIdentityCards);
		
		ChartSeries returned = new ChartSeries();
		returned.setLabel("An der Ausleihe");
		returned.set("Spiele", numberOfAvailableGames - numberOfCurrentLendGames);
		returned.set("Ausweise", numberOfAvailableIdentityCards - numberOfCurrentLendIdentityCards);
		
		model.addSeries(lend);
		model.addSeries(returned);
		
		return model;
	}
}
