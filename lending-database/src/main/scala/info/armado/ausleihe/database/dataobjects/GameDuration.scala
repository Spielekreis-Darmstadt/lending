package info.armado.ausleihe.database.dataobjects

import info.armado.ausleihe.database.util.JPAAnnotations._
import javax.persistence.Embeddable

object GameDuration {
  def apply(singleDuration: Int): GameDuration = new GameDuration(singleDuration)
}

/**
  * A GameDuration, which contains the minimum and maximum time needed to play a specific game.
  *
  * @constructor Creates a new GameDuration instance
  * @param minDuration The minimum time needed to play a specific game
  * @param maxDuration The maximum time needed to play a specific game
  */
@Embeddable
case class GameDuration(
    @BeanProperty @Column var minDuration: Integer,
    @BeanProperty @Column var maxDuration: Integer) extends Serializable {

  /**
    * Create a new empty GameDuration instance
    *
    * Required for JPA
    */
  def this() = this(null, null)

  /**
    * Create a new GameDuration instance with a given single duration time.
    * This given duration time is taken as both the minimum duration and the maximum duration.
    *
    * @param singleDuration The duration time needed to play a specific game
    */
  def this(singleDuration: Int) = this(singleDuration, singleDuration)

  /**
    * Creates a copy of this GameDuration instance
    */
  def copy = GameDuration(minDuration, maxDuration)
  
  override def toString: String = if (minDuration == maxDuration) s"$minDuration" else s"$minDuration - $maxDuration"
}