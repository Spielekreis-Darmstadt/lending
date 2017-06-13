package info.armado.ausleihe.faces.beans;

import java.util.HashMap;
import java.util.List;

public interface ObjectHandlerFunction {
	void addToDatabase(List<XLSRow> rows, HashMap<String, Integer> felder);
}
