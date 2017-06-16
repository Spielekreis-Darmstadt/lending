/**
 * 
 */
package info.armado.ausleihe.faces.beans.envelopes;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import info.armado.ausleihe.database.access.EnvelopeDAO;
import info.armado.ausleihe.database.entities.Envelope;
import lombok.Getter;
import lombok.Setter;

/**
 * @author marc
 *
 *
 */
@Named("showEnvelopes")
@ViewScoped
public class ShowEnvelopesBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EnvelopeDAO envelopeDao;

	@Getter
	private List<Envelope> envelopes;

	@Getter
	@Setter
	private List<Envelope> filteredEnvelopes;

	@PostConstruct
	public void init() {
		this.envelopes = envelopeDao.selectAll();
	}
}
