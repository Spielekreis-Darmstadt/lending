package info.armado.ausleihe.database.dataobjects

import info.armado.ausleihe.database.util.JPAAnnotations._
import javax.persistence.Embeddable

@Embeddable
case class PlayerCount(
    @BeanProperty @Column var minPlayerCount: Integer,
    @BeanProperty @Column var maxPlayerCount: Integer) {
  
  def this() = this(null, null)
  def this(singlePlayerCount: Int) = this(singlePlayerCount, singlePlayerCount)
  
  def copy = PlayerCount(minPlayerCount, maxPlayerCount)
  
  override def toString(): String = if (minPlayerCount == maxPlayerCount) s"$minPlayerCount" else s"$minPlayerCount - $maxPlayerCount" 
}