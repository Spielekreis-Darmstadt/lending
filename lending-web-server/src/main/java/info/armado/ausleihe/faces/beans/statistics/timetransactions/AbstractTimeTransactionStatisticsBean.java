/**
 * 
 */
package info.armado.ausleihe.faces.beans.statistics.timetransactions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import info.armado.ausleihe.database.access.LendGameDAO;
import info.armado.ausleihe.database.access.LendIdentityCardDAO;
import info.armado.ausleihe.database.dataobjects.LendGame;
import info.armado.ausleihe.database.dataobjects.LendIdentityCard;
import lombok.Getter;
import lombok.Setter;

/**
 * @author marc
 *
 */
public abstract class AbstractTimeTransactionStatisticsBean {
	
	@Inject
	protected LendGameDAO lendGameDao;

	@Inject
	protected LendIdentityCardDAO lendIdentityCardDao;

	@Getter
	protected LineChartModel lineChartModel;

	@Getter @Setter
	protected LocalDate day;

	public void loadSelectedLineChartModel() {
		this.lineChartModel = createLineChartModel(day);
	}
	
	protected LineChartModel createLineChartModel(LocalDate day) {
		LineChartModel model = new LineChartModel();

		List<LendGame> lendGames = lendGameDao.selectAll();
		List<LendIdentityCard> lendIdentityCards = lendIdentityCardDao.selectAll();

		Map<Integer, Long> numberOfLendGames = getNumbersOfLendGamesPerHour(lendGames, day);
		Map<Integer, Long> numberOfLendIdCards = getNumbersOfLendIdCardsPerHour(lendIdentityCards, day);

		Map<Integer, Long> numberOfReturnedGames = getNumbersOfReturnedGamesPerHour(lendGames, day);
		Map<Integer, Long> numberOfReturnedIdCards = getNumbersOfReturnedIdCardsPerHour(lendIdentityCards, day);

		// Setting min and max hour
		Set<Integer> times = new HashSet<Integer>();
		times.addAll(numberOfLendGames.keySet());
		times.addAll(numberOfLendIdCards.keySet());
		times.addAll(numberOfReturnedGames.keySet());
		times.addAll(numberOfReturnedIdCards.keySet());
		OptionalInt minHour = times.stream().mapToInt(Integer::intValue).min();
		OptionalInt maxHour = times.stream().mapToInt(Integer::intValue).max();

		// Setting max amount
		List<Entry<Integer, Long>> amounts = new ArrayList<Entry<Integer, Long>>();
		amounts.addAll(numberOfLendGames.entrySet());
		amounts.addAll(numberOfLendIdCards.entrySet());
		amounts.addAll(numberOfReturnedGames.entrySet());
		amounts.addAll(numberOfReturnedIdCards.entrySet());
		OptionalLong maxAmount = amounts.stream()
				.collect(Collectors.groupingBy(entry -> entry.getKey(),
						Collectors.summingLong(entry -> entry.getValue())))
				.values().stream().mapToLong(Long::longValue).max();

		LineChartSeries lendGamesSeries = createLineChartSeries("Verliehene Spiele", numberOfLendGames, minHour,
				maxHour);
		LineChartSeries lendIdCardsSeries = createLineChartSeries("Vergebene Ausweise", numberOfLendIdCards, minHour,
				maxHour);
		LineChartSeries returnedGamesSeries = createLineChartSeries("Zurückgegebene Spiele", numberOfReturnedGames,
				minHour, maxHour);
		LineChartSeries returnedIdCardsSeries = createLineChartSeries("Zurückgegebene Ausweise",
				numberOfReturnedIdCards, minHour, maxHour);

		model.addSeries(lendGamesSeries);
		model.addSeries(returnedGamesSeries);
		model.addSeries(lendIdCardsSeries);
		model.addSeries(returnedIdCardsSeries);

		model.setTitle("Anzahl Vorgänge");
		model.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
		model.setLegendPosition("s");
		model.setStacked(true);
		model.setShowPointLabels(true);

		Axis xAxis = new CategoryAxis("Stunden");
		model.getAxes().put(AxisType.X, xAxis);
		Axis yAxis = model.getAxis(AxisType.Y);
		yAxis.setLabel("Anzahl");
		yAxis.setMin(0);
		yAxis.setMax(1);
		if (maxAmount.isPresent()) {
			long yMax = (long) (Math.ceil(maxAmount.getAsLong() * 1.1));
			yAxis.setMax(yMax);
		}

		return model;
	}

	private LineChartSeries createLineChartSeries(String label, Map<Integer, Long> values, OptionalInt minHour,
			OptionalInt maxHour) {
		LineChartSeries series = new LineChartSeries();
		series.setFill(true);
		series.setLabel(label);

		if (minHour.isPresent() && maxHour.isPresent())
			IntStream.rangeClosed(minHour.getAsInt(), maxHour.getAsInt()).forEach(hour -> {
				long numberOfTransactions = 0;
				if (values.containsKey(hour))
					numberOfTransactions = values.get(hour);

				series.set(Integer.toString(hour) + " Uhr", numberOfTransactions);
			});

		return series;
	}

	private Map<Integer, Long> getNumbersOfLendGamesPerHour(List<LendGame> lendGames, LocalDate date) {
		return lendGames.stream().filter(game -> game.getLendTime().toLocalDate().equals(date))
				.map(game -> game.getLendTime()).map(time -> time.getHour())
				.collect(Collectors.groupingBy(time -> time, Collectors.counting()));
	}

	private Map<Integer, Long> getNumbersOfReturnedGamesPerHour(List<LendGame> lendGames, LocalDate date) {
		return lendGames.stream().filter(game -> game.getReturnTime() != null)
				.filter(game -> game.getReturnTime().toLocalDate().equals(date)).map(game -> game.getReturnTime())
				.map(time -> time.getHour()).collect(Collectors.groupingBy(time -> time, Collectors.counting()));
	}

	private Map<Integer, Long> getNumbersOfLendIdCardsPerHour(List<LendIdentityCard> lendIdentityCard, LocalDate date) {
		return lendIdentityCard.stream().filter(game -> game.getLendTime().toLocalDate().equals(date))
				.map(game -> game.getLendTime()).map(time -> time.getHour())
				.collect(Collectors.groupingBy(time -> time, Collectors.counting()));
	}

	private Map<Integer, Long> getNumbersOfReturnedIdCardsPerHour(List<LendIdentityCard> lendIdentityCard,
			LocalDate date) {
		return lendIdentityCard.stream().filter(game -> game.getReturnTime() != null)
				.filter(game -> game.getReturnTime().toLocalDate().equals(date)).map(game -> game.getReturnTime())
				.map(time -> time.getHour()).collect(Collectors.groupingBy(time -> time, Collectors.counting()));
	}
}
