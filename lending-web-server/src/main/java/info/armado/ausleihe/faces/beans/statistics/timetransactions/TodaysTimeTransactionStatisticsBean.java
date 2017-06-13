/**
 * 
 */
package info.armado.ausleihe.faces.beans.statistics.timetransactions;

import java.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * @author marc
 *
 */
@Named("todaysTimeTransactionStatistics")
@RequestScoped
public class TodaysTimeTransactionStatisticsBean extends AbstractTimeTransactionStatisticsBean {
	
	@PostConstruct
	private void init() {
		this.day = LocalDate.now();
		this.loadSelectedLineChartModel();
	}
}
