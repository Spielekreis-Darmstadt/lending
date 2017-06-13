/**
 * 
 */
package info.armado.ausleihe.database.objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class is used for game group information retrieval.
 * It contains all available information about a group of games with a given name.
 * Currently this contains only the number of games with the given name that are contained in the database
 * 
 * @author Arndt
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="title")
public class GameInfo {
	/**
	 * The title of the games
	 */
	@Getter @Setter
	private String title;
	
	/**
	 * The number of games with the same title contained in the database
	 */
	@Getter @Setter
	private long count;
}
