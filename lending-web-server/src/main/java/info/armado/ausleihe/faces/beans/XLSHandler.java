/**
 * 
 */
package info.armado.ausleihe.faces.beans;

import info.armado.ausleihe.extensions.PoiExtensions;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.model.UploadedFile;

/**
 * @author Arndt
 *
 */
@ExtensionMethod(value = {PoiExtensions.class})
public class XLSHandler implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Workbook workbook;
	
	private Sheet sheet;
	
	@Getter
	@Setter
	private String selectedSheet;
	
	@Getter
	private Map<Integer, XLSColumn> columnHeaders;
	
	@Getter
	private List<XLSRow> rows;

	public void loadWorkbook(UploadedFile file) throws IOException, InvalidFormatException {
		this.workbook = WorkbookFactory.create(file.getInputstream());
	}

	public String[] getSheets() {
		int numberOfSheets = workbook.getNumberOfSheets();
		String[] sheets = new String[numberOfSheets];
		for (int i = 0; i < numberOfSheets; i++)
			sheets[i] = workbook.getSheetName(i);

		return sheets;
	}

	public void selectSheet(AdderInterface adder) {
		this.sheet = workbook.getSheet(selectedSheet);
		this.columnHeaders = extractColumnNames(adder);
		this.rows = extractRows();
	}
	
	public XLSColumn[] getColumnNames() {
		return columnHeaders.values().toArray(new XLSColumn[0]);
	}

	private Map<Integer, XLSColumn> extractColumnNames(AdderInterface adder) {
		int firstRow = sheet.getFirstRowNum();
		int numberOfRows = sheet.getLastRowNum() + 1 - firstRow;
		
		Map<Integer, XLSColumn> result = new HashMap<Integer, XLSColumn>();
		
		// If there is at least one row in the excel file
		if (numberOfRows > 1) {	
			Row row = sheet.getRow(firstRow);
			int firstColumn = row.getFirstCellNum();
			int lastColumn = row.getLastCellNum();
			
			for (int index = firstColumn; index < lastColumn; index++) {
				result.put(index, new XLSColumn(row.getCell(index).asString(), index, adder.getDefaultDatabaseFieldName()));
			}
		} 
		
		return result;
	}

	private List<XLSRow> extractRows() {
		// the row interval
		int firstRow = sheet.getFirstRowNum() + 1;
		int lastRow = sheet.getLastRowNum() + 1;
		
		Map<Integer, XLSColumn> columnNames = columnHeaders; 
		
		List<XLSRow> rows = new ArrayList<XLSRow>();
		for (int rowIndex = firstRow; rowIndex < lastRow; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			
			// the row doesn't exist
			if (row == null) continue;
			
			// the column interval
			int firstColumn = row.getFirstCellNum();
			int lastColumn = row.getLastCellNum();
			
			// the column is empty
			if (firstColumn == -1) continue;
			
			// Map in the form: XLS Column -> Cell value
			HashMap<XLSColumn, String> values = new HashMap<XLSColumn, String>();
			for (int columnIndex = firstColumn; columnIndex < lastColumn; columnIndex++) {
				// Columnheader
				XLSColumn column = columnNames.get(columnIndex);
				// Cell value or null if the cell is undefined
				String value = row.getCell(column.getXlsColumn()).asString();
				values.put(column, value);
			}
			rows.add(new XLSRow(values));
		}
		
		return rows;
	}

	public void validateIDs(int xlsIdColumn) {
		HashSet<String> containedIds = new HashSet<String>();
		int rowId = 1;
		for (XLSRow row : rows) {
			String id = row.getValue(xlsIdColumn);
						
			if (id == null || id.isEmpty()) {
				// There is no id value contained in the row
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, String.format("Leere ID \"%s\" in Zeile %d.", id, rowId), "");
	            FacesContext context = FacesContext.getCurrentInstance();
	            context.addMessage("mappingMessages", message);
			} else if (containedIds.contains(id)) {
				// The id value is contained in at least two rows
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, String.format("Doppelte ID \"%s\" in Zeile %d.", id, rowId), "");
	            FacesContext context = FacesContext.getCurrentInstance();
	            context.addMessage("mappingMessages", message);
			} else {
				containedIds.add(id);
			}
			
			rowId++;
		}
	}
}
