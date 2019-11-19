package info.armado.ausleihe.client.transport.results

import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class LendingEntityNotExistsDTO(var barcode: String) extends AbstractResultDTO {
  def this() = this(null)
}
