package info.armado.ausleihe.remote.results

import javax.xml.bind.annotation.XmlRootElement
import scala.beans.BeanProperty

object LendingEntityNotExists {
  def apply(barcode: String): LendingEntityNotExists = {
    val lendingEntityExists = new LendingEntityNotExists()

    lendingEntityExists.barcode = barcode

    lendingEntityExists
  }

  def unapply(lendingEntityExists: LendingEntityNotExists): Option[String] =
    Some(lendingEntityExists.barcode)
}

@XmlRootElement
class LendingEntityNotExists extends AbstractResult {
  @BeanProperty
  var barcode: String = _

  override def equals(other: Any): Boolean = {
    val Barcode = barcode

    other match {
      case LendingEntityNotExists(Barcode) => true
      case _ => false
    }
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + (if (barcode == null) 0 else barcode.hashCode)

    result
  }
  
  override def toString: String = s"LendingEntityNotExists($barcode)"
}