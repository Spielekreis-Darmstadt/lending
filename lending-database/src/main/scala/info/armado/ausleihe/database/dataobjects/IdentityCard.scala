package info.armado.ausleihe.database.dataobjects

import javax.persistence._

import info.armado.ausleihe.database.util.JPAAnnotations._

/**
  * Factory for [[IdentityCard]] instances.
  */
object IdentityCard {
  /**
    * Creates an IdentityCard instance with a given barcode and availability state
    *
    * @param barcode   The barcode of the identity card
    * @param available The availability state of the identity card
    * @return The new IdentityCard instance
    */
  def apply(barcode: Barcode, available: Boolean): IdentityCard = new IdentityCard(0, barcode, available)

  /**
    * Creates a tuple containing the barcode and the availability state for a given identity card
    *
    * @param identityCard The identity card
    * @return The new tuple containing the barcode and availability state of the identity card
    */
  def unapply(identityCard: IdentityCard): Option[(Barcode, Boolean)] = Some((identityCard.barcode, identityCard.available))
}

/**
  * An identity card, which can be bound to an [[Envelope]]
  *
  * @param id        The unique identifier of the identity card
  * @param barcode   The barcode of the identity card
  * @param available The availability state of the identity card
  */
@Entity
@Table
class IdentityCard(@BeanProperty @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int,
                   @BeanProperty @Column(unique = true, nullable = false) var barcode: Barcode,
                   @BeanProperty @Column var available: Boolean) extends Serializable {

  /**
    * Create a new empty IdentityCard instance
    *
    * Required for JPA
    */
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