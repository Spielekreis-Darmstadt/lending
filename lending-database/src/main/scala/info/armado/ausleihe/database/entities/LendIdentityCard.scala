package info.armado.ausleihe.database.entities

import java.time.LocalDateTime
import java.util.{ArrayList, List => JList}
import javax.persistence._

import info.armado.ausleihe.database.util.JPAAnnotations._

import scala.collection.JavaConverters._

/**
  * Factory for [[LendIdentityCard]] instances.
  */
object LendIdentityCard {
  /**
    * Creates a new LendIdentityCard instance with a given identity card, envelope, lend time and owner of the identity card.
    * This method assumes that identity card is currently still issued.
    *
    * @param identityCard The identity card, which is issued
    * @param envelope     The envelope, which is issued
    * @param lendTime     The time when the then identity card has been issued
    * @param owner        The owner of the identity card
    * @return The new LendIdentityCard instance
    */
  def apply(identityCard: IdentityCard, envelope: Envelope, lendTime: LocalDateTime, owner: String): LendIdentityCard =
    LendIdentityCard(identityCard, envelope, lendTime, null, owner)

  /**
    * Creates a new LendIdentityCard instance with a given identity card, envelope, lend time and owner of the identity card.
    * This method assumes that identity card is currently still issued.
    * In addition this method assumes that the identity card has an anonymous owner
    *
    * @param identityCard The identity card, which is issued
    * @param envelope     The envelope, which is issued
    * @param lendTime     The time when the then identity card has been issued
    * @return The new LendIdentityCard instance
    */
  def apply(identityCard: IdentityCard, envelope: Envelope, lendTime: LocalDateTime): LendIdentityCard =
    LendIdentityCard(identityCard, envelope, lendTime, null, null)
}

/**
  * An identity card [[IdentityCard]] issued to an envelope [[Envelope]].
  *
  * @author Marc Arndt
  * @constructor Creates a new LendIdentityCard instance with a given id, identity card, envelope, list of borrowed games, issue time, return time and owner
  * @param identityCard The identity card which is issued
  * @param envelope     The envelope to which the identity card is bound
  * @param lendTime     The time at which the identity card has been issued
  * @param returnTime   The time at which the identity card has been returned
  * @param owner        The owner of the issued identity card
  */
@Entity
@Table
case class LendIdentityCard(@BeanProperty @ManyToOne(optional = false) var identityCard: IdentityCard,
                            @BeanProperty @ManyToOne(optional = false) var envelope: Envelope,
                            @BeanProperty @Column(nullable = false) var lendTime: LocalDateTime,
                            @BeanProperty @Column var returnTime: LocalDateTime,
                            @BeanProperty @Column var owner: String) extends Serializable {

  /**
    * The unique identifier of the issued identity card
    */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private var id: Int = _

  /**
    * A list of all borrowed games by the issued identity card
    */
  @OneToMany(mappedBy = "lendIdentityCard")
  private var _lendGames: JList[LendGame] = new ArrayList[LendGame]

  /**
    * Create a new empty LendIdentityCard instance
    *
    * Required for JPA
    */
  def this() = this(null, null, null, null, null)

  def lendGames: List[LendGame] = _lendGames.asScala.toList

  /**
    * A list of currently borrowed games by this issued identity card.
    */
  @Transient
  def currentLendGames: List[LendGame] = lendGames.filter(_.returnTime == null)

  /**
    * True if this issued identity card has no games currently borrowed
    */
  @Transient
  def hasNoCurrentLendGames: Boolean = currentLendGames.isEmpty

  /**
    * True if this issued identity card has at least one game currently borrowed
    */
  @Transient
  def hasCurrentLendGames: Boolean = currentLendGames.nonEmpty

  override def equals(other: Any): Boolean = other match {
    case other: LendIdentityCard => other.isInstanceOf[LendIdentityCard] && this.hashCode == other.hashCode
    case _ => false
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + identityCard.hashCode
    result = prime * result + envelope.hashCode
    result = prime * result + lendTime.hashCode
    result = prime * result + Option(returnTime).hashCode
    result = prime * result + Option(owner).hashCode

    result
  }
}