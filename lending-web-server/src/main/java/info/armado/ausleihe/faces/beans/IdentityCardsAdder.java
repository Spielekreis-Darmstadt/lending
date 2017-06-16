/**
 * 
 */
package info.armado.ausleihe.faces.beans;

import info.armado.ausleihe.database.access.IdentityCardDAO;
import info.armado.ausleihe.database.barcode.Barcode;
import info.armado.ausleihe.database.barcode.Barcode$;
import info.armado.ausleihe.database.entities.IdentityCard;
import info.armado.ausleihe.database.enums.Prefix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author marc
 *
 */
public class IdentityCardsAdder extends AdderInterface {

	private IdentityCardDAO idCardDao;
	
	private boolean identityCardsAvailable;
	
	public IdentityCardsAdder(IdentityCardDAO idCardDao, boolean identityCardsAvailable) {
		super("AID");
		this.idCardDao = idCardDao;
		this.identityCardsAvailable = identityCardsAvailable;
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

		List<IdentityCard> identityCards = new ArrayList<IdentityCard>(rows.size());
		// create the Game objects
		for (XLSRow row : rows) {
			IdentityCard idCard = new IdentityCard();
			// set the sid
			String id = row.getValue(felder.get(idFieldName));
			if (Barcode$.MODULE$.valid(id))
				idCard.setBarcode(new Barcode(id));
			else
				idCard.setBarcode(new Barcode(Prefix.IdentityCards, Integer.parseInt(id)));

			// set the available state of the new idcard
			idCard.setAvailable(identityCardsAvailable);
			
			identityCards.add(idCard);
		}
		
		Set<Barcode> alreadyPersistedBarcodes = idCardDao.selectAllIdentityCardsAlreadyPersisted(identityCards);
		
		if (alreadyPersistedBarcodes.isEmpty()) idCardDao.insert(identityCards);
		else {
			for (Barcode barcode : alreadyPersistedBarcodes) {
				this.addFacesErrorMessage(String.format(
						"Barcode ist bereits in der Datenbank enthalten: \"%s\"", barcode.toString()));
			}
		}
	}

}
