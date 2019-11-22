package info.armado.ausleihe.database.dataobjects

import info.armado.ausleihe.database.util.JPAAnnotations._
import javax.persistence.Embeddable

object PlayerCount {
  def apply(singlePlayerCount: Int): PlayerCount = new PlayerCount(singlePlayerCount)
}

/**
  * A PlayerCount, which contains the minimum and maximum number of people that can play a specific game.
  *
  * @constructor Creates a new PlayerCount instance
  * @param minPlayerCount The minimum number of people that can play a specific game
  * @param maxPlayerCount The maximum number of people that can play a specific game
  */
@Embeddable
case class PlayerCount(@Column var minPlayerCount: Integer, @Column var maxPlayerCount: Integer) {

  /**
    * Create a new empty PlayerCount instance
    *
    * Required for JPA
    */
  def this() = this(null, null)

  /**
    * Creates a new PlayerCount instance with a single number of people that can play a specific game.
    * This number of people is taken as both the minimum and maximum number of people that can play the specific game.
    *
    * @param singlePlayerCount The number of people that can play a specific game
    */
  def this(singlePlayerCount: Int) = this(singlePlayerCount, singlePlayerCount)

  /**
    * Creates a copy of this PlayerCount instance
    */
  def copy = PlayerCount(minPlayerCount, maxPlayerCount)
}
