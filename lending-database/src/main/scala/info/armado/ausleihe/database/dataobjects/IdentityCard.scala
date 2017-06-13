package info.armado.ausleihe.database.dataobjects

import scala.beans.BeanProperty
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

object IdentityCard {
  def apply(barcode: Barcode, available: Boolean): IdentityCard = {
    val identityCard = new IdentityCard
    
    identityCard.barcode = barcode
    identityCard.available = available
    
    identityCard
  }
  
  def unapply(identityCard: IdentityCard): Option[(Barcode, Boolean)] = Some((identityCard.barcode, identityCard.available))
}

@Entity
@Table
class IdentityCard {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @BeanProperty
  var id: Int = _

  @Column(unique = true, nullable = false)
  @BeanProperty
  var barcode: Barcode = _

  @Column
  @BeanProperty
  var available: Boolean = _

  override def equals(other: Any): Boolean = other match {
    case other: IdentityCard => other.isInstanceOf[IdentityCard] && this.barcode == other.barcode
    case _ => false
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1
    result = prime * result + (if (barcode == null) 0 else barcode.hashCode)
    return result
  }
}