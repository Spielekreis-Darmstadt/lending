/**
 * 
 */
package info.armado.ausleihe.faces.beans.identitycards;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import info.armado.ausleihe.database.access.IdentityCardDAO;
import info.armado.ausleihe.database.access.LendIdentityCardDAO;
import info.armado.ausleihe.database.dataobjects.IdentityCard;
import info.armado.ausleihe.database.dataobjects.LendGame;
import info.armado.ausleihe.database.dataobjects.LendIdentityCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author marc
 *
 *
 */
@Named("showIdentityCards")
@ViewScoped
public class ShowIdentityCardsBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private LendIdentityCardDAO lendIdentityCardDao;

	@Inject
	private IdentityCardDAO identityCardDao;

	@Getter
	private List<IdentityCardWrapper> idCards;

	@Getter
	@Setter
	private List<IdentityCardWrapper> filteredIdCards;

	@Getter
	@Setter
	private IdentityCardWrapper selectedIdCard;

	@PostConstruct
	public void init() {
		List<IdentityCard> idCards = identityCardDao.selectAll();
		Map<IdentityCard, LendIdentityCard> currentLendIdCards = lendIdentityCardDao.selectAllCurrentlyLend().stream()
				.collect(Collectors.toMap(lendIdentityCard -> lendIdentityCard.getIdentityCard(),
						lendIdentityCard -> lendIdentityCard));

		this.idCards = idCards.stream().map(idCard -> {
			IdentityCardWrapper result = new IdentityCardWrapper();

			result.setIdCard(idCard);
			// The idcard is currently issued
			if (currentLendIdCards.containsKey(idCard))
				result.setOwner(currentLendIdCards.get(idCard).getOwner());

			return result;
		}).collect(Collectors.toList());
	}

	@Transactional
	public TimelineModel getModel() {
		TimelineModel model = new TimelineModel();

		if (!lendIdentityCardDao.isIdentityCardIssued(selectedIdCard.getIdCard()))
			return model;

		LendIdentityCard lic = lendIdentityCardDao.selectCurrentByIdentityCard(selectedIdCard.getIdCard());
		model.add(new TimelineEvent("Ausweisausgabe",
				Date.from(lic.getLendTime().atZone(ZoneId.systemDefault()).toInstant()), false, "Verliehen", "lend"));

		List<LendGame> lendGames = lic.getAllLendGames();
		lendGames.forEach(lendGame -> {
			TimelineEvent timelineEvent = null;
			if (lendGame.getReturnTime() == null) {
				timelineEvent = new TimelineEvent(
						String.format("<p>%s</p><p>%s</p>", lendGame.getGame().getBarcode().toString(),
								lendGame.getGame().getTitle()),
						Date.from(lendGame.getLendTime().atZone(ZoneId.systemDefault()).toInstant()));
				timelineEvent.setGroup("Verliehen");
				timelineEvent.setStyleClass("lend");
			} else {
				timelineEvent = new TimelineEvent(
						String.format("<p>%s</p><p>%s</p>", lendGame.getGame().getBarcode().toString(),
								lendGame.getGame().getTitle()),
						Date.from(lendGame.getLendTime().atZone(ZoneId.systemDefault()).toInstant()),
						Date.from(lendGame.getReturnTime().atZone(ZoneId.systemDefault()).toInstant()));
				timelineEvent.setGroup("Zurückgegeben");
				timelineEvent.setStyleClass("returned");
			}
			model.add(timelineEvent);
		});

		return model;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	public class IdentityCardWrapper {
		@Getter
		@Setter
		private IdentityCard idCard;

		@Getter
		@Setter
		private String owner;
	}
}
