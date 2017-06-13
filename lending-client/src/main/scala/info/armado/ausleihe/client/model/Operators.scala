package info.armado.ausleihe.client.model

object Operators extends Enumeration {
  val Spielekreis, BDKJ = Value

  def parse(string: String) = string.toLowerCase match {
    case "spielekreis" => Some(Operators.Spielekreis)
    case "bdkj" => Some(Operators.BDKJ)
    case _ => None
  }
}