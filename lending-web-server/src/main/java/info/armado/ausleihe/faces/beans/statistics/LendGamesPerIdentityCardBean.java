/**
 * 
 */
package info.armado.ausleihe.faces.beans.statistics;

import java.util.Map;
import java.util.OptionalLong;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import info.armado.ausleihe.database.access.LendGameDAO;
import lombok.Getter;

/**
 * Represents the backend for the chart that shows how many games were lend by
 * how many identity cards.
 * 
 * @author marc
 *
 */
@Named("lendGamesPerIdentityCard")
@RequestScoped
public class LendGamesPerIdentityCardBean {

	@Inject
	private LendGameDAO lendGameDao;

	@Getter
	private LineChartModel model;

	@PostConstruct
	private void init() {
		this.model = new LineChartModel();

		Map<Long, Long> verteilung = lendGameDao.selectAll().stream()
				.collect(Collectors.groupingBy(lendGame -> lendGame.getLendIdentityCard(), Collectors.counting()))
				.values().stream().collect(Collectors.groupingBy(amount -> amount, Collectors.counting()));

		LineChartSeries series = new LineChartSeries();
		series.setLabel("Anzahl Ausweise mit der Anzahl jeweils ausgeliehener Spiele");
		verteilung.forEach((games, idChars) -> series.set(games, idChars));

		model.addSeries(series);
		
		model.setTitle("Anzahl Ausweise mit verliehenen Spielen");
		model.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
		model.setLegendPosition("s");
		model.setShowPointLabels(true);

		Axis xAxis = new CategoryAxis("Anzahl verliehener Spiele");
		model.getAxes().put(AxisType.X, xAxis);
		Axis yAxis = model.getAxis(AxisType.Y);
		yAxis.setLabel("Anzahl Ausweise");
		yAxis.setMin(0);
		yAxis.setMax(1);
		OptionalLong max = verteilung.values().stream().mapToLong(Long::longValue).max();
		if (max.isPresent())
			yAxis.setMax(max.getAsLong() * 1.1);
	}
}
