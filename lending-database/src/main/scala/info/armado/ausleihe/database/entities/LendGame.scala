package info.armado.ausleihe.database.entities

import java.time.LocalDateTime
import javax.persistence._

import info.armado.ausleihe.database.util.JPAAnnotations._

/**
  * Factory for [[LendGame]] instances.
  */
object LendGame {
  /**
    * Creates a LendGame instance for a given game, issued identity card and lend time.
    * This method assumes that the game is currently still borrowed.
    *
    * @param game             The game, which is borrowed
    * @param lendIdentityCard The issued identity card, that borrows the game
    * @param lendTime         The data time when the game has been borrowed
    * @return The new LendGame instance
    */
  def apply(game: Game, lendIdentityCard: LendIdentityCard, lendTime: LocalDateTime): LendGame =
    LendGame(game, lendIdentityCard, lendTime, null)
}

/**
  * A game [[Game]] which is currently borrowed by an issued identity card [[LendIdentityCard]].
  *
  * @author Marc Arndt
  * @constructor Create a new LendGame instance with a given id, game, issued identity card, lend time and return time
  * @param game             The game, which is borrowed
  * @param lendIdentityCard The issued identity card, that borrows the game
  * @param lendTime         The data time when the game has been borrowed
  * @param returnTime       The data time when the game has been returned
  */
@Entity
@Table
case class LendGame(@BeanProperty @ManyToOne(optional = false) var game: Game,
                    @BeanProperty @ManyToOne(optional = false) var lendIdentityCard: LendIdentityCard,
                    @BeanProperty @Column(nullable = false) var lendTime: LocalDateTime,
                    @BeanProperty @Column var returnTime: LocalDateTime) extends Serializable {

  /**
    * The unique identifier of the LendGame instance
    */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private var id: Int = _

  /**
    * Create a new empty LendGame instance
    *
    * Required for JPA
    */
  def this() = this(null, null, null, null)

  @Transient
  def currentlyBorrowed: Boolean = Option(returnTime).nonEmpty

  override def equals(other: Any): Boolean = other match {
    case other: LendGame =>
      this.game == other.game &&
      this.lendIdentityCard == other.lendIdentityCard &&
      this.lendTime == other.lendTime &&
      Option(this.returnTime) == Option(other.returnTime)
    case _ => false
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + game.hashCode
    result = prime * result + lendIdentityCard.hashCode
    result = prime * result + lendTime.hashCode
    result = prime * result + Option(returnTime).hashCode

    result
  }
}