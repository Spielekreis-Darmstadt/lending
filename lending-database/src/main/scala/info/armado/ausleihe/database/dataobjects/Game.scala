package info.armado.ausleihe.database.dataobjects

import java.time.Year
import java.util.{ List => JList }
import scala.beans.BeanProperty
import javax.persistence.Table
import javax.persistence.Entity
import info.armado.ausleihe.database.util.JPAAnnotations._
import javax.persistence.GenerationType
import java.util.ArrayList


object Game {
  def apply(barcode: Barcode): Game = Game(0, barcode, null, null, null, null, null, null, null, null, false, new ArrayList[LendGame]()) 
  def apply(barcode: Barcode, title: String, author: String, publisher: String, available: Boolean): Game = Game(0, barcode, title, author, publisher, null, null, null, null, null, available, new ArrayList[LendGame]())
}

@Entity
@Table
case class Game(
  @BeanProperty @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int,
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
  
  def createOnBasis = new Game(0, null, title, author, publisher, Option(playerCount).map { _.copy }.orNull, Option(gameDuration).map { _.copy }.orNull, minimumAge, releaseYear, comment, available, new ArrayList[LendGame]())

  def this() = this(0, null, null, null, null, null, null, null, null, null, false, new ArrayList[LendGame]()) 
  
  override def equals(other: Any): Boolean = other match {
    case other: Game => other.isInstanceOf[Game] && this.barcode == other.barcode
    case _ => false
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1
    result = prime * result + (if (barcode == null) 0 else barcode.hashCode)
    return result
  }
}