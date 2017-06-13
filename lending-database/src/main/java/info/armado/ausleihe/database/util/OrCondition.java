/**
 * 
 */
package info.armado.ausleihe.database.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author marc
 *
 */
public class OrCondition implements Condition {
	private List<Condition> innerConditions;
	
	public OrCondition() {
		this.innerConditions = new ArrayList<>();
	}
	
	public OrCondition(Condition... innerConditions) {
		this.innerConditions = Arrays.asList(innerConditions);
	}
	
	public void addCondition(Condition condition) {
		this.innerConditions.add(condition);
	}
	
	@Override
	public String toString() {		
		return innerConditions.stream().map(condition -> condition.toString()).collect(Collectors.joining(" or ", "(", ")"));
	}

	@Override
	public boolean isEmpty() {
		return innerConditions.isEmpty();
	}
}
