/**
 * 
 */
package info.armado.ausleihe.faces.beans.statistics.timetransactions;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author marc
 *
 */
@Named("timeTransactionStatistics")
@ViewScoped
public class TimeTransactionStatisticsBean extends AbstractTimeTransactionStatisticsBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter
	private List<LocalDateWrapper> possibleDays;

	@PostConstruct
	private void init() {
		this.day = null;
		this.lineChartModel = null;
		this.possibleDays = createPossibleDays();			
	}
	
	@Override
	public void loadSelectedLineChartModel() {
		super.loadSelectedLineChartModel();
		
		this.lineChartModel.setZoom(true);
	}
	
	private List<LocalDateWrapper> createPossibleDays() {
		Set<LocalDate> lendGameDates = lendGameDao.selectAll().stream()
				.map(lendGame -> lendGame.getLendTime().toLocalDate())
				.collect(Collectors.toSet());
		Set<LocalDate> returnGameDates = lendGameDao.selectAll().stream()
				.filter(lendGame -> lendGame.getReturnTime() != null)
				.map(lendGame -> lendGame.getReturnTime().toLocalDate())
				.collect(Collectors.toSet());
		Set<LocalDate> lendIdentityCardDates = lendIdentityCardDao.selectAll().stream()
				.map(lendIdentityCard -> lendIdentityCard.getLendTime().toLocalDate())
				.collect(Collectors.toSet());
		Set<LocalDate> returnIdentityCardDates = lendIdentityCardDao.selectAll().stream()
				.filter(lendIdentityCard -> lendIdentityCard.getReturnTime() != null)
				.map(lendIdentityCard -> lendIdentityCard.getReturnTime().toLocalDate())
				.collect(Collectors.toSet());
		
		Set<LocalDate> dates = new HashSet<LocalDate>();
		dates.addAll(lendGameDates);
		dates.addAll(returnGameDates);
		dates.addAll(lendIdentityCardDates);
		dates.addAll(returnIdentityCardDates);
		
		return dates.stream().sorted().map(day -> new LocalDateWrapper(day)).collect(Collectors.toList()); 
	}
	
	@AllArgsConstructor
	public class LocalDateWrapper {
		@Getter
		private LocalDate day;
		
		public String getFormattedString() {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMANY);
			
			return day.format(formatter);
		}
	}
}
