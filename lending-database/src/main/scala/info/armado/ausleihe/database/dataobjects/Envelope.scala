package info.armado.ausleihe.database.dataobjects

import scala.beans.BeanProperty
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.GenerationType
import javax.persistence.Table
import javax.persistence.Entity

object Envelope {
  def apply(barcode: Barcode, available: Boolean = false): Envelope = {
    val envelope = new Envelope
    
    envelope.barcode = barcode
    envelope.available = available
    
    envelope
  }
  
  def unapply(envelope: Envelope): Option[(Barcode, Boolean)] = Some((envelope.barcode, envelope.available))
}

@Entity
@Table
class Envelope extends Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @BeanProperty
  var id: Int = _

  @Column(unique=true, nullable=false)
  @BeanProperty
  var barcode: Barcode = _

  @Column
  @BeanProperty
  var available: Boolean = _
  
  override def equals(other: Any): Boolean = other match {
    case other: Envelope => other.isInstanceOf[Envelope] && this.barcode == other.barcode 
    case _ => false
  }
  
  override def hashCode: Int = {
    val prime = 31
    var result = 1
    result = prime * result + (if (barcode == null) 0 else barcode.hashCode)
    return result
  }
}