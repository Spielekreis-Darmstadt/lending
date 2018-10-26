package info.armado.ausleihe.remote.client.results

import javax.xml.bind.annotation.XmlRootElement

import scala.beans.BeanProperty

object LendingEntityNotExistsDTO {
  def apply(barcode: String): LendingEntityNotExistsDTO = {
    val lendingEntityExists = new LendingEntityNotExistsDTO()

    lendingEntityExists.barcode = barcode

    lendingEntityExists
  }

  def unapply(lendingEntityExists: LendingEntityNotExistsDTO): Option[String] =
    Some(lendingEntityExists.barcode)
}

@XmlRootElement
class LendingEntityNotExistsDTO extends AbstractResultDTO {
  @BeanProperty
  var barcode: String = _

  override def equals(other: Any): Boolean = {
    val Barcode = barcode

    other match {
      case LendingEntityNotExistsDTO(Barcode) => true
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