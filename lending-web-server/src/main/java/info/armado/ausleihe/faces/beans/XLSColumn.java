package info.armado.ausleihe.faces.beans;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class XLSColumn implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The name of this column in the xls (excel) file
	 */
	@Getter
	@Setter
	private String xlsName;
	
	/**
	 * The id of this column in the xls (excel) file
	 */
	@Getter
	@Setter
	private int xlsColumn;
	
	/**
	 * The associated database column to this xls (excel) column
	 */
	@Getter
	@Setter
	private String databaseName;
	
	public XLSColumn(String xlsName, int xlsColumn, String defaultDatabaseName) {
		this.xlsName = xlsName;
		this.xlsColumn = xlsColumn;
		this.databaseName = defaultDatabaseName;
	}
}
