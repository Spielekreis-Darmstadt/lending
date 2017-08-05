package info.armado.ausleihe.database.entities

import javax.persistence._

import info.armado.ausleihe.database.barcode.Barcode
import info.armado.ausleihe.database.util.JPAAnnotations._

/**
  * An envelope, which can be bound to an [[IdentityCard]]
  *
  * @author Marc Arndt
  * @constructor Create a new Envelope instance with a given id, barcode and availability state
  * @param barcode   The barcode of the envelope
  * @param available The availability state of the envelope
  */
@Entity
@Table
case class Envelope(@BeanProperty @Column(unique = true, nullable = false) var barcode: Barcode,
                    @BeanProperty @Column var available: Boolean) extends HasBarcode with Serializable {

  /**
    * The unique identifier of the Envelope instance
    */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private var id: Int = _

  /**
    * Create a new empty Envelope instance
    *
    * Required for JPA
    */
  def this() = this(null, false)

  override def equals(other: Any): Boolean = other match {
    case other: Envelope => this.barcode == other.barcode
    case _ => false
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + barcode.hashCode

    result
  }
}