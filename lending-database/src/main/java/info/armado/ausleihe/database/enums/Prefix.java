/**
 * 
 */
package info.armado.ausleihe.database.enums;

/**
 * @author marc
 *
 */
public enum Prefix {
	Spielekreis("11") {
		@Override
		public String toString() {
			return "Spiele des Spielekreises";
		}
	},
	
	BDKJ("22") {
		@Override
		public String toString() {
			return "Spiele des BDKJs";
		}
	},
	
	IdentityCards("33") {
		@Override
		public String toString() {
			return "Ausleihausweise";
		}
	},
	
	Envelopes("44") {
		@Override
		public String toString() {
			return "Ausleihumschläge";
		}
	},
	
	Any("__") {
		@Override
		public String toString() {
			return "Alle Barcodes";
		}
	},
	
	Unknown("00") {
		@Override
		public String toString() {
			return "Unbekannt";
		}
	};
	
	private String prefix;
	
	Prefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public static Prefix parse(String prefix) {
		switch (prefix) {
		case "11":
			return Prefix.Spielekreis;
		case "22":
			return Prefix.BDKJ;
		case "33":
			return Prefix.IdentityCards;
		case "44":
			return Prefix.Envelopes;
		default:
			return Prefix.Unknown;
		}
	}
}
