package info.armado.ausleihe.database.dataobjects

import java.util.{ List => JList }
import java.time.LocalDateTime
import scala.beans.BeanProperty
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.ManyToOne
import javax.persistence.Column
import javax.persistence.OneToMany
import javax.persistence.Transient
import scala.collection.JavaConverters._
import javax.persistence.Entity
import javax.persistence.Table
import java.util.ArrayList

object LendIdentityCard {
  def apply(identityCard: IdentityCard, envelope: Envelope, lendTime: LocalDateTime, returnTime: LocalDateTime, owner: String = null): LendIdentityCard = {
    val lendIdentityCard = LendIdentityCard(identityCard, envelope, lendTime, owner)

    lendIdentityCard.returnTime = returnTime

    lendIdentityCard
  }
  
  def apply(identityCard: IdentityCard, envelope: Envelope, lendTime: LocalDateTime, owner: String): LendIdentityCard = {
    val lendIdentityCard = LendIdentityCard(identityCard, envelope, lendTime)

    lendIdentityCard.owner = owner

    lendIdentityCard
  }
  
  def apply(identityCard: IdentityCard, envelope: Envelope, lendTime: LocalDateTime): LendIdentityCard = {
    val lendIdentityCard = new LendIdentityCard

    lendIdentityCard.identityCard = identityCard
    lendIdentityCard.envelope = envelope
    lendIdentityCard.lendTime = lendTime

    lendIdentityCard
  }

  def unapply(lendIdentityCard: LendIdentityCard): Option[(IdentityCard, Envelope, LocalDateTime, LocalDateTime, String, List[LendGame])] =
    Some((lendIdentityCard.identityCard, lendIdentityCard.envelope, lendIdentityCard.lendTime, lendIdentityCard.returnTime, lendIdentityCard.owner, lendIdentityCard.currentLendGames))
}

@Entity
@Table
class LendIdentityCard {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @BeanProperty
  var id: Int = _

  @ManyToOne(optional = false)
  @BeanProperty
  var identityCard: IdentityCard = _

  @ManyToOne(optional = false)
  @BeanProperty
  var envelope: Envelope = _

  @OneToMany(mappedBy = "lendIdentityCard")
  @BeanProperty
  var allLendGames: JList[LendGame] = new ArrayList[LendGame]

  @Column(nullable = false)
  @BeanProperty
  var lendTime: LocalDateTime = _

  @Column
  @BeanProperty
  var returnTime: LocalDateTime = _

  /**
   * The name of the owner of this card, or null if no owner is set
   */
  @Column
  @BeanProperty
  var owner: String = _

  @Transient
  def currentLendGames: List[LendGame] = allLendGames.asScala.filter { _.returnTime == null }.toList

  @Transient
  def getCurrentLendGames: JList[LendGame] = currentLendGames.asJava;

  override def equals(other: Any): Boolean = other match {
    case other: LendIdentityCard => other.isInstanceOf[LendIdentityCard] && this.id == other.id
    case _ => false
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1
    result = prime * result + id;
    return result
  }
}