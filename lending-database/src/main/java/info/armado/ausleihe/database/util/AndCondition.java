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
public class AndCondition implements Condition {
	private List<Condition> innerConditions;
	
	public AndCondition() {
		this.innerConditions = new ArrayList<>();
	}
	
	public AndCondition(Condition... innerConditions) {
		this.innerConditions = Arrays.asList(innerConditions);
	}
	
	public void addCondition(Condition condition) {
		this.innerConditions.add(condition);
	}
	
	@Override
	public String toString() {
		return innerConditions.stream().map(condition -> condition.toString()).collect(Collectors.joining(" and ", "(", ")"));
	}

	@Override
	public boolean isEmpty() {
		return innerConditions.isEmpty();
	}
}
