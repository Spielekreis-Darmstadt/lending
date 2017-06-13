/**
 * 
 */
package info.armado.ausleihe.faces.beans.envelopes;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import info.armado.ausleihe.database.access.EnvelopeDAO;
import info.armado.ausleihe.database.dataobjects.Envelope;
import lombok.Getter;
import lombok.Setter;

/**
 * @author marc
 *
 */
@Named("activateEnvelope")
@ViewScoped
public class ActivateEnvelopeBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EnvelopeDAO envelopeDao;

	@Getter
	@Setter
	private Envelope envelope;

	@Transactional
	public void activateEnvelope() {
		envelope.setAvailable(true);
		envelopeDao.update(envelope);

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Umschlag aktiviert",
				String.format("Der Umschlag \"%s\" wurde erfolgreich aktiviert.", envelope.getBarcode().toString()));
		FacesContext.getCurrentInstance().addMessage("messages", message);

		// Inputreset
		envelope = null;
	}
}
