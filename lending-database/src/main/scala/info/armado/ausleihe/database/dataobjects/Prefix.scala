package info.armado.ausleihe.database.dataobjects

object Prefix {
  def apply(prefix: String): Prefix = prefix match {
    case "11" => Spielekreis
    case "22" => BDKJ
    case "33" => IdentityCards
    case "44" => Envelopes
    case _ => Unknown
  }

  val Spielekreis = Prefix("11", "Spiele des Spielekreises")
  val BDKJ = Prefix("22", "Spiele des BDKJs")

  val IdentityCards = Prefix("33", "Ausweise")

  val Envelopes = Prefix("44", "Umschläge")

  val Any = Prefix("__", "Alle Barcodes")

  val Unknown = Prefix("00", "Unbekannt")
}

case class Prefix(prefix: String, name: String)
