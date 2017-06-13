/**
 * 
 */
package info.armado.ausleihe.faces.beans.envelopes;

import info.armado.ausleihe.database.access.EnvelopeDAO;
import info.armado.ausleihe.database.access.LendIdentityCardDAO;
import info.armado.ausleihe.database.dataobjects.Envelope;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

/**
 * @author marc
 *
 */
@Named("deactivateEnvelope")
@ViewScoped
public class DeactivateEnvelopeBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EnvelopeDAO envelopeDao;

	@Inject
	private LendIdentityCardDAO lendIdentityCardDao;

	@Getter
	@Setter
	private Envelope selectedEnvelope;

	@Getter
	@Setter
	private List<Envelope> envelopes;

	@PostConstruct
	public void init() {
		this.envelopes = envelopeDao.selectAllAvailable();
	}

	public List<Envelope> completeEnvelope(String query) {
		return envelopes.stream().filter(envelope -> envelope.getBarcode().toString().startsWith(query))
				.collect(Collectors.toList());
	}

	public void save() {
		FacesMessage message = null;
		if (!lendIdentityCardDao.isEnvelopeIssued(selectedEnvelope)) {
			selectedEnvelope.setAvailable(false);
			envelopeDao.update(selectedEnvelope);

			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Umschlag deaktiviert", String.format(
					"Der Umschlag \"%s\" wurde erfolgreich deaktiviert.", selectedEnvelope.getBarcode().toString()));
			
			// Inputreset
			selectedEnvelope = null;
		} else {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Umschlag ist ausgegeben", String
					.format("Der Umschlag \"%s\" ist zur Zeit ausgegeben.", selectedEnvelope.getBarcode().toString()));
		}
		FacesContext.getCurrentInstance().addMessage("messages", message);
	}
}
