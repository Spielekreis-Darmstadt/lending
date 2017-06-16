package info.armado.ausleihe.database.dataobjects

import java.time.Year
import java.util.{ArrayList, List => JList}
import javax.persistence.{Entity, GenerationType, Table, Transient}

import info.armado.ausleihe.database.util.JPAAnnotations._

import scala.collection.JavaConverters._

object Game {
  def apply(barcode: Barcode): Game = new Game(0, barcode, null, null, null, null, null, null, null, null, false, new ArrayList[LendGame])

  def apply(barcode: Barcode, title: String, author: String, publisher: String, available: Boolean): Game =
    new Game(0, barcode, title, author, publisher, null, null, null, null, null, available, new ArrayList[LendGame])

  def unapply(game: Game): Option[(Barcode, String, String, String, PlayerCount, GameDuration, Integer, Year, String, Boolean, Option[LendGame])] =
    Some((game.barcode, game.title, game.author, game.publisher, game.playerCount, game.gameDuration, game.minimumAge,
      game.releaseYear, game.comment, game.available, game.currentLendGame))
}

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

  def this() = this(0, null, null, null, null, null, null, null, null, null, false, new ArrayList[LendGame])

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