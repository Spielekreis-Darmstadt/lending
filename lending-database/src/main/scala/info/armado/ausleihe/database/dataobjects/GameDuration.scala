package info.armado.ausleihe.database.dataobjects

import info.armado.ausleihe.database.util.JPAAnnotations._
import javax.persistence.Embeddable

@Embeddable
case class GameDuration(
    @BeanProperty @Column var minDuration: Integer,
    @BeanProperty @Column var maxDuration: Integer) {
  
  def this() = this(null, null)
  def this(singleDuration: Int) = this(singleDuration, singleDuration) 
  
  def copy = new GameDuration(minDuration, maxDuration)
  
  override def toString(): String = if (minDuration == maxDuration) s"$minDuration" else s"$minDuration - $maxDuration" 
}