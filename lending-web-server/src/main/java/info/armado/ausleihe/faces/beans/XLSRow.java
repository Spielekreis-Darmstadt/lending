package info.armado.ausleihe.faces.beans;

import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class XLSRow {
	/**
	 * Map with the structure:
	 * XLS Column -> Cell value
	 */
	@Getter
	private HashMap<XLSColumn, String> values;
	
	public String getValue(XLSColumn column) {
		return values.get(column);
	}
	
	public String getValue(String key) {
		return values.entrySet().stream()
				.filter(entry -> entry.getKey().getXlsName().equals(key))
				.findAny().orElseThrow(() -> new IllegalArgumentException("Eine Spalte mit dem übergebenen Key existiert nicht."))
				.getValue();
	}
	
	public String getValue(int index) {
		return values.entrySet().stream()
				.filter(entry -> entry.getKey().getXlsColumn() == index)
				.findAny().orElseThrow(() -> new IllegalArgumentException("Eine Spalte mit dem übergebenen Index existiert nicht."))
				.getValue();
	}
	
	public int getSize() {
		return values.size();
	}
}
