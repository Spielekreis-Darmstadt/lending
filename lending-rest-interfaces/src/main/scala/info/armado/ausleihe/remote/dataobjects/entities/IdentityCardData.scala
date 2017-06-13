package info.armado.ausleihe.remote.dataobjects.entities

import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.bind.annotation.XmlAccessType
import info.armado.ausleihe.remote.util.Annotations._

object IdentityCardData {
  def apply(barcode: String, owner: String): IdentityCardData = new IdentityCardData(barcode, owner)
  def apply(barcode: String): IdentityCardData = new IdentityCardData(barcode, null)
  
  def unapply(identityCardData: IdentityCardData): Option[(String, Option[String])] = 
    Some((identityCardData.barcode, Option(identityCardData.owner)))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class IdentityCardData(
    @BeanProperty var barcode: String,
    @BeanProperty var owner: String) extends LendingEntity {
  
  def this() = this(null, null)
  
  override def equals(other: Any): Boolean = {
    val Barcode = barcode
    val Owner = Option(owner)
    
    other match {
      case IdentityCardData(Barcode, Owner) => true
      case _ => false
    }
  }
  
  override def hashCode: Int = {
    val prime = 31
    var result = 1
    
    result = prime * result + (if (barcode == null) 0 else barcode.hashCode)
    result = prime * result + (if (owner == null) 0 else owner.hashCode)
    
    return result
  }
  
  override def toString: String = s"IdentityCardData($barcode, ${Option(owner)})"
}