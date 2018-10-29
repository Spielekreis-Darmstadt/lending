package info.armado.ausleihe.database.entities

import java.time.Year
import java.util.{ArrayList, List => JList}
import javax.persistence.{Entity, GenerationType, Table, Transient}

import info.armado.ausleihe.database.barcode.Barcode
import info.armado.ausleihe.database.dataobjects._
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
  def apply(barcode: Barcode): Game = Game(barcode, null, null, null, null, null, null, null, null, false)

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
    Game(barcode, title, author, publisher, null, null, null, null, null, available)
}

/**
  * A game, which can be issued/bound to a [[LendIdentityCard]]
  *
  * @author Marc Arndt
  * @constructor Creates a new Game instance
  * @param barcode      The barcode of the game
  * @param title        The title of the game
  * @param author       The author of the game
  * @param publisher    The publisher of the game
  * @param playerCount  The number of players that can play the game
  * @param gameDuration The time duration required to play the game
  * @param minimumAge   The minimum age required to play the game
  * @param releaseYear  The release year, when the game has been published
  * @param comment      A comment about the game
  * @param available    The availability state of the game
  */
@Entity
@Table
case class Game(@Column(unique = true, nullable = false) var barcode: Barcode,
                @Column var title: String,
                @Column var author: String,
                @Column var publisher: String,
                @Embedded var playerCount: PlayerCount,
                @Embedded var gameDuration: GameDuration,
                @Column var minimumAge: Integer,
                @Column var releaseYear: Year,
                @Column var comment: String,
                @Column var available: Boolean) extends HasBarcode with Serializable {

  /**
    * The unique identifier of the Game instance
    */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private var id: Int = _

  /**
    * An internal list of [[LendGame]] instances for the Game instance.
    *
    * Required for JPA
    */
  @OneToMany(mappedBy = "game")
  private var _identityCardLendings: JList[LendGame] = new ArrayList[LendGame]

  /**
    * Create a new empty Game instance
    *
    * Required for JPA
    */
  def this() = this(null, null, null, null, null, null, null, null, null, false)

  /**
    * A list of [[LendGame]] instances for the game
    */
  @Transient
  def lendings: List[LendGame] = _identityCardLendings.asScala.toList

  /**
    * The current [[LendGame]] instance, to which this game belongs
    */
  @Transient
  def currentLending: Option[LendGame] = lendings.find(_.currentlyBorrowed)

  override def equals(other: Any): Boolean = other match {
    case other: Game => this.barcode == other.barcode
    case _ => false
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + barcode.hashCode

    result
  }
}