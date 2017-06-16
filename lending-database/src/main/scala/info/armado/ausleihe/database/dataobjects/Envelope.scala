package info.armado.ausleihe.database.dataobjects

import javax.persistence._

import info.armado.ausleihe.database.util.JPAAnnotations._

/**
  * Factory for [[Envelope]] instances.
  */
object Envelope {
  /**
    * Creates an Envelope instance, with a given barcode and a given availability state
    *
    * @param barcode   The barcode of the new envelope
    * @param available The availability state of the new envelope
    * @return The new Envelope instance
    */
  def apply(barcode: Barcode, available: Boolean = false): Envelope = new Envelope(0, barcode, available)

  /**
    * Creates a tuple containing the barcode and the availability state for a given envelope
    *
    * @param envelope The envelope
    * @return The new tuple containing the barcode and availability state of the envelope
    */
  def unapply(envelope: Envelope): Option[(Barcode, Boolean)] = Some((envelope.barcode, envelope.available))
}

/**
  * An envelope, which can be bound to an [[IdentityCard]]
  *
  * @constructor Create a new Envelope instance with a given id, barcode and availability state
  * @param id        The unique identifier of the envelope
  * @param barcode   The barcode of the envelope
  * @param available The availability state of the envelope
  */
@Entity
@Table
class Envelope(@BeanProperty @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int,
               @BeanProperty @Column(unique = true, nullable = false) var barcode: Barcode,
               @BeanProperty @Column var available: Boolean) extends Serializable {

  /**
    * Required for JPA
    *
    * @constructor Create a new empty Envelope instance
    */
  def this() = this(0, null, false)

  override def equals(other: Any): Boolean = other match {
    case other: Envelope => other.isInstanceOf[Envelope] && this.barcode == other.barcode
    case _ => false
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + (if (barcode == null) 0 else barcode.hashCode)

    result
  }
}