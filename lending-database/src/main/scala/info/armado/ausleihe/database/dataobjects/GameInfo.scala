package info.armado.ausleihe.database.dataobjects

case class GameInfo(title: String, count: Long) {
  def this() = this(null, 0)
}
