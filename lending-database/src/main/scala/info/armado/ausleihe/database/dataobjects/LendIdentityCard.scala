package info.armado.ausleihe.database.dataobjects

import java.time.LocalDateTime
import java.util.{List => JList, ArrayList}
import javax.persistence._

import info.armado.ausleihe.database.util.JPAAnnotations._

import scala.collection.JavaConverters._

object LendIdentityCard {
  def apply(identityCard: IdentityCard, envelope: Envelope, lendTime: LocalDateTime, returnTime: LocalDateTime, owner: String = null): LendIdentityCard =
    new LendIdentityCard(0, identityCard, envelope, new ArrayList[LendGame], lendTime, returnTime, owner)

  def apply(identityCard: IdentityCard, envelope: Envelope, lendTime: LocalDateTime, owner: String): LendIdentityCard =
    new LendIdentityCard(0, identityCard, envelope, new ArrayList[LendGame], lendTime, null, owner)

  def apply(identityCard: IdentityCard, envelope: Envelope, lendTime: LocalDateTime): LendIdentityCard =
    new LendIdentityCard(0, identityCard, envelope, new ArrayList[LendGame], lendTime, null, null)

  def unapply(lendIdentityCard: LendIdentityCard): Option[(IdentityCard, Envelope, LocalDateTime, LocalDateTime, String, List[LendGame])] =
    Some((lendIdentityCard.identityCard, lendIdentityCard.envelope, lendIdentityCard.lendTime, lendIdentityCard.returnTime, lendIdentityCard.owner, lendIdentityCard.currentLendGames))
}

@Entity
@Table
class LendIdentityCard(@BeanProperty @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int,
                       @BeanProperty @ManyToOne(optional = false) var identityCard: IdentityCard,
                       @BeanProperty @ManyToOne(optional = false) var envelope: Envelope,
                       @BeanProperty @OneToMany(mappedBy = "lendIdentityCard") var allLendGames: JList[LendGame],
                       @BeanProperty @Column(nullable = false) var lendTime: LocalDateTime,
                       @BeanProperty @Column var returnTime: LocalDateTime,
                       @BeanProperty @Column var owner: String) extends Serializable {

  def this() = this(0, null, null, new ArrayList[LendGame], null, null, null)

  @Transient
  def currentLendGames: List[LendGame] = allLendGames.asScala.filter {
    _.returnTime == null
  }.toList

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