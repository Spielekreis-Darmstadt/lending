package info.armado.ausleihe.database.dataobjects

import java.time.LocalDateTime
import javax.persistence._

import info.armado.ausleihe.database.util.JPAAnnotations._

/**
  * Factory for [[LendGame]] instances.
  */
object LendGame {
  /**
    * Creates a LendGame instance for a given game, issued identity card, lend time and return time.
    *
    * @param game             The game, which is borrowed
    * @param lendIdentityCard The issued identity card, that borrows the game
    * @param lendTime         The data time when the game has been borrowed
    * @param returnTime       The data time when the game has been returned
    * @return The new LendGame instance
    */
  def apply(game: Game, lendIdentityCard: LendIdentityCard, lendTime: LocalDateTime, returnTime: LocalDateTime): LendGame =
    new LendGame(0, game, lendIdentityCard, lendTime, returnTime)

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
    new LendGame(0, game, lendIdentityCard, lendTime, null)

  /**
    * Creates a tuple containing the game, issued identity card, lend time and return time for a given lend game.
    *
    * @param lendGame The lend game
    * @return The new tuple containing the information of the given lend game
    */
  def unapply(lendGame: LendGame): Option[(Game, LendIdentityCard, LocalDateTime, LocalDateTime)] =
    Some((lendGame.game, lendGame.lendIdentityCard, lendGame.lendTime, lendGame.returnTime))
}

/**
  * A game [[Game]] which is currently borrowed by an issued identity card [[LendIdentityCard]].
  *
  * @constructor Create a new LendGame instance with a given id, game, issued identity card, lend time and return time
  * @param id               The unique identifier of the lend game
  * @param game             The game, which is borrowed
  * @param lendIdentityCard The issued identity card, that borrows the game
  * @param lendTime         The data time when the game has been borrowed
  * @param returnTime       The data time when the game has been returned
  */
@Entity
@Table
class LendGame(@BeanProperty @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int,
               @BeanProperty @ManyToOne(optional = false) var game: Game,
               @BeanProperty @ManyToOne(optional = false) var lendIdentityCard: LendIdentityCard,
               @BeanProperty @Column(nullable = false) var lendTime: LocalDateTime,
               @BeanProperty @Column var returnTime: LocalDateTime) extends Serializable {

  /**
    * Required for JPA
    *
    * @constructor Create a new empty LendGame instance
    */
  def this() = this(0, null, null, null, null)

  @Transient
  def isCurrentlyBorrowed(): Boolean = Option(returnTime).nonEmpty

  override def equals(other: Any): Boolean = other match {
    case other: LendGame => other.isInstanceOf[LendGame] && this.id == other.id
    case _ => false
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + id

    result
  }
}