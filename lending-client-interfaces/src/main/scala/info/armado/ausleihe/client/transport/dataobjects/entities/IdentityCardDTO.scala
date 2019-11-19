package info.armado.ausleihe.client.transport.dataobjects.entities

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object IdentityCardDTO {
  def apply(barcode: String, owner: String): IdentityCardDTO = new IdentityCardDTO(barcode, owner)

  def apply(barcode: String): IdentityCardDTO = new IdentityCardDTO(barcode, null)

  def unapply(identityCardData: IdentityCardDTO): Option[(String, Option[String])] =
    Some((identityCardData.barcode, Option(identityCardData.owner)))
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class IdentityCardDTO(var barcode: String, var owner: String) extends LendingEntityDTO {

  def this() = this(null, null)

  override def equals(other: Any): Boolean = {
    val Barcode = barcode
    val Owner = Option(owner)

    other match {
      case IdentityCardDTO(Barcode, Owner) => true
      case _                               => false
    }
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + (if (barcode == null) 0 else barcode.hashCode)
    result = prime * result + (if (owner == null) 0 else owner.hashCode)

    result
  }

  override def toString: String = s"IdentityCardData($barcode, ${Option(owner)})"
}
