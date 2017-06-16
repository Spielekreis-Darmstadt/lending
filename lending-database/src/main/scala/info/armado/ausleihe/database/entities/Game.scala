package info.armado.ausleihe.database.entities

import java.time.Year
import java.util.{ArrayList, List => JList}
import javax.persistence.{Entity, GenerationType, Table, Transient}

import info.armado.ausleihe.database.dataobjects._
import info.armado.ausleihe.database.barcode.Barcode
import info.armado.ausleihe.database.util.JPAAnnotations._

import scala.collection.JavaConverters._

/**
  * Factory for [[Game]] instances.
  */
object Game {
  /**
    * Creates a Game instance with a given barcode
    *
    * @param barcode The barcode of the game
    * @return The new Game instance
    */
  def apply(barcode: Barcode): Game = new Game(0, barcode, null, null, null, null, null, null, null, null, false, new ArrayList[LendGame])

  /**
    * Creates a Game instance with a given barcode, an availability state and other game specific information
    *
    * @param barcode   The barcode of the game
    * @param title     The title of the game
    * @param author    The author of the game
    * @param publisher The publisher of the game
    * @param available The availability state of the game
    * @return The new Game instance
    */
  def apply(barcode: Barcode, title: String, author: String, publisher: String, available: Boolean): Game =
    new Game(0, barcode, title, author, publisher, null, null, null, null, null, available, new ArrayList[LendGame])

  /**
    * Creates a tuple containing the information of a given game
    *
    * @param game The game
    * @return The new tuple containing the game information
    */
  def unapply(game: Game): Option[(Barcode, String, String, String, PlayerCount, GameDuration, Integer, Year, String, Boolean, Option[LendGame])] =
    Some((game.barcode, game.title, game.author, game.publisher, game.playerCount, game.gameDuration, game.minimumAge,
      game.releaseYear, game.comment, game.available, game.currentLendGame))
}

/**
  * A game, which can be issued/bound to a [[LendIdentityCard]]
  *
  * @constructor Creates a new Game instance
  * @param id                   The unique identifier of the Game instance
  * @param barcode              The barcode of the game
  * @param title                The title of the game
  * @param author               The author of the game
  * @param publisher            The publisher of the game
  * @param playerCount          The number of players that can play the game
  * @param gameDuration         The time duration required to play the game
  * @param minimumAge           The minimum age required to play the game
  * @param releaseYear          The release year, when the game has been published
  * @param comment              A comment about the game
  * @param available            The availability state of the game
  * @param identityCardLendings A list of [[LendGame]] instances for the game
  */
@Entity
@Table
class Game(@BeanProperty @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int,
           @BeanProperty @Column(unique = true, nullable = false) var barcode: Barcode,
           @BeanProperty @Column var title: String,
           @BeanProperty @Column var author: String,
           @BeanProperty @Column var publisher: String,
           @BeanProperty @Embedded var playerCount: PlayerCount,
           @BeanProperty @Embedded var gameDuration: GameDuration,
           @BeanProperty @Column var minimumAge: Integer,
           @BeanProperty @Column var releaseYear: Year,
           @BeanProperty @Column var comment: String,
           @BeanProperty @Column var available: Boolean,
           @BeanProperty @OneToMany(mappedBy = "game") var identityCardLendings: JList[LendGame]) extends Serializable {

  def createOnBasis = new Game(0, null, title, author, publisher, Option(playerCount).map(_.copy).orNull,
    Option(gameDuration).map(_.copy).orNull, minimumAge, releaseYear, comment, available, new ArrayList[LendGame])

  /**
    * Create a new empty Game instance
    *
    * Required for JPA
    */
  def this() = this(0, null, null, null, null, null, null, null, null, null, false, new ArrayList[LendGame])

  /**
    * The current [[LendGame]] instance, to which this game belongs
    */
  @Transient
  def currentLendGame: Option[LendGame] = identityCardLendings.asScala.find(_.isCurrentlyBorrowed())

  override def equals(other: Any): Boolean = other match {
    case other: Game => other.isInstanceOf[Game] && this.barcode == other.barcode
    case _ => false
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + (if (barcode == null) 0 else barcode.hashCode)

    result
  }
}