/**
 * 
 */
package info.armado.ausleihe.database.util;

/**
 * @author marc
 *
 */
public class StringCondition implements Condition {
	private String condition;
	
	public StringCondition(String condition) {
		this.condition = condition;
	}
	
	@Override
	public String toString() {
		return condition;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}
