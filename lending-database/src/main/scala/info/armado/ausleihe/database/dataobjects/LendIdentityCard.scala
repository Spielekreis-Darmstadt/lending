package info.armado.ausleihe.database.dataobjects

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
    * Creates a new LendIdentityCard instance with a given identity card, envelope, lend time, return time and owner of the identity card.
    *
    * @param identityCard The identity card, which is issued
    * @param envelope     The envelope, which is bound to the identity card
    * @param lendTime     The time when the identity card has been issued
    * @param returnTime   The time when the identity card has been returned
    * @param owner        The owner of the identity card
    * @return The new LendIdentityCard instance
    */
  def apply(identityCard: IdentityCard, envelope: Envelope, lendTime: LocalDateTime, returnTime: LocalDateTime, owner: String = null): LendIdentityCard =
    new LendIdentityCard(0, identityCard, envelope, new ArrayList[LendGame], lendTime, returnTime, owner)

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
    new LendIdentityCard(0, identityCard, envelope, new ArrayList[LendGame], lendTime, null, owner)

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
    new LendIdentityCard(0, identityCard, envelope, new ArrayList[LendGame], lendTime, null, null)

  /**
    * Creates a tuple containing the identity card, envelope, lend time and return time, owner and a list with all borrowed games for a given issued identity card.
    *
    * @param lendIdentityCard The issued identity card
    * @return The new tuple containing the information of the given issued identity card
    */
  def unapply(lendIdentityCard: LendIdentityCard): Option[(IdentityCard, Envelope, LocalDateTime, LocalDateTime, String, List[LendGame])] =
    Some((lendIdentityCard.identityCard, lendIdentityCard.envelope, lendIdentityCard.lendTime, lendIdentityCard.returnTime, lendIdentityCard.owner, lendIdentityCard.currentLendGames))
}

/**
  * An identity card [[IdentityCard]] issued to an envelope [[Envelope]].
  *
  * @constructor Creates a new LendIdentityCard instance with a given id, identity card, envelope, list of borrowed games, issue time, return time and owner
  * @param id           The unique identifier of the issued identity card
  * @param identityCard The identity card which is issued
  * @param envelope     The envelope to which the identity card is bound
  * @param allLendGames A list of all borrowed games by the issued identity card
  * @param lendTime     The time at which the identity card has been issued
  * @param returnTime   The time at which the identity card has been returned
  * @param owner        The owner of the issued identity card
  */
@Entity
@Table
class LendIdentityCard(@BeanProperty @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int,
                       @BeanProperty @ManyToOne(optional = false) var identityCard: IdentityCard,
                       @BeanProperty @ManyToOne(optional = false) var envelope: Envelope,
                       @BeanProperty @OneToMany(mappedBy = "lendIdentityCard") var allLendGames: JList[LendGame],
                       @BeanProperty @Column(nullable = false) var lendTime: LocalDateTime,
                       @BeanProperty @Column var returnTime: LocalDateTime,
                       @BeanProperty @Column var owner: String) extends Serializable {

  /**
    * Create a new empty LendIdentityCard instance
    *
    * Required for JPA
    */
  def this() = this(0, null, null, new ArrayList[LendGame], null, null, null)

  /**
    * A list of currently borrowed games by this issued identity card.
    */
  @Transient
  def currentLendGames: List[LendGame] = allLendGames.asScala.filter {
    _.returnTime == null
  }.toList

  /**
    * @return The list of currently borrowed games by this issued identity card as a [[JList]]
    */
  @Transient
  def getCurrentLendGames: JList[LendGame] = currentLendGames.asJava

  override def equals(other: Any): Boolean = other match {
    case other: LendIdentityCard => other.isInstanceOf[LendIdentityCard] && this.id == other.id
    case _ => false
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + id

    result
  }
}