package info.armado.ausleihe.client.transport.results

import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class IncorrectBarcodeDTO(var barcode: String) extends AbstractResultDTO {
  def this() = this(null)
}
