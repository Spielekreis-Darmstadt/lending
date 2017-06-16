package info.armado.ausleihe.database.dataobjects

import javax.persistence._

import info.armado.ausleihe.database.util.JPAAnnotations._

object IdentityCard {
  def apply(barcode: Barcode, available: Boolean): IdentityCard = new IdentityCard(0, barcode, available)

  def unapply(identityCard: IdentityCard): Option[(Barcode, Boolean)] = Some((identityCard.barcode, identityCard.available))
}

@Entity
@Table
class IdentityCard(@BeanProperty @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int,
                   @BeanProperty @Column(unique = true, nullable = false) var barcode: Barcode,
                   @BeanProperty @Column var available: Boolean) extends Serializable {

  def this() = this(0, null, false)

  override def equals(other: Any): Boolean = other match {
    case other: IdentityCard => other.isInstanceOf[IdentityCard] && this.barcode == other.barcode
    case _ => false
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + (if (barcode == null) 0 else barcode.hashCode)

    result
  }
}