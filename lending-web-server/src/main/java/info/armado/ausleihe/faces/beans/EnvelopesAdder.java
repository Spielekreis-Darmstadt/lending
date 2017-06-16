/**
 * 
 */
package info.armado.ausleihe.faces.beans;

import info.armado.ausleihe.database.access.EnvelopeDAO;
import info.armado.ausleihe.database.barcode.Barcode;
import info.armado.ausleihe.database.barcode.Barcode$;
import info.armado.ausleihe.database.entities.Envelope;
import info.armado.ausleihe.database.enums.Prefix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author marc
 *
 */
public class EnvelopesAdder extends AdderInterface {

	private EnvelopeDAO envelopeDao;
	
	private boolean envelopesAvailable;
	
	public EnvelopesAdder(EnvelopeDAO envelopeDao, boolean envelopesAvailable) {
		super("UID");
		this.envelopeDao = envelopeDao;
		this.envelopesAvailable = envelopesAvailable;
	}
	
	/* (non-Javadoc)
	 * @see info.armado.ausleihe.faces.beans.AdderInterface#getDatabaseFieldNames()
	 */
	@Override
	public String[] getDatabaseFieldNames() {
		return new String[] { idFieldName, notNeeded };
	}
	
	@Override
	public String getDefaultDatabaseFieldName() {
		return notNeeded;
	}

	/* (non-Javadoc)
	 * @see info.armado.ausleihe.faces.beans.AdderInterface#addItems(java.util.List, java.util.Map)
	 */
	@Override
	public void addItems(List<XLSRow> rows, Map<String, Integer> felder) {
		if (!felder.containsKey(idFieldName))
			return;

		List<Envelope> envelopes = new ArrayList<Envelope>(rows.size());
		// create the Game objects
		for (XLSRow row : rows) {
			Envelope envelope = new Envelope();
			// set the sid
			String id = row.getValue(felder.get(idFieldName));
			if (Barcode$.MODULE$.valid(id))
				envelope.setBarcode(new Barcode(id));
			else
				envelope.setBarcode(new Barcode(Prefix.Envelopes, Integer.parseInt(id)));

			// set the available state of the new envelope
			envelope.setAvailable(envelopesAvailable);
			
			envelopes.add(envelope);
		}
		
		Set<Barcode> alreadyPersistedBarcodes = envelopeDao.selectAllEnvelopesAlreadyPersisted(envelopes);
		
		if (alreadyPersistedBarcodes.isEmpty()) envelopeDao.insert(envelopes);
		else {
			for (Barcode barcode : alreadyPersistedBarcodes) {
				this.addFacesErrorMessage(String.format(
						"Barcode ist bereits in der Datenbank enthalten: \"%s\"", barcode.toString()));
			}
		}
	}

}
