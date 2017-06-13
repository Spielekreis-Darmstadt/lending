/**
 * 
 */
package info.armado.ausleihe.extensions;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

/**
 * @author marc
 *
 */
public class PoiExtensions {
	/**
	 * This method returns the content of the cell as a string.
	 * To call this method it's not important to know which type the content of the cell is 
	 * @param cell	The cell, which content is wanted
	 * @return		The content of the cell as a string
	 */
	public static String asString(Cell cell) {
		// if the cell is null, in which case it is undefined, return null
		if (cell == null)
			return null;

		String value = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				value = cell.getDateCellValue().toString();
			} else {
				double number = cell.getNumericCellValue();
				if ((number == Math.floor(number))
						&& !Double.isInfinite(number)) {
					value = String.valueOf((int) number);
				} else {
					value = String.valueOf(number);
				}
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			// Ist bereits String
			value = cell.getCellFormula(); 
			break;
		case Cell.CELL_TYPE_ERROR:
			value = "Error: " + String.valueOf(cell.getErrorCellValue());
			break;
		case Cell.CELL_TYPE_BLANK:
			value = null;
			break;
		default:
		}
		return value;
	}
}
