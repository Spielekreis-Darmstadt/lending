package info.armado.ausleihe.client.transport.results

import info.armado.ausleihe.client.transport.util.Annotations._
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class IncorrectBarcodeDTO(@BeanProperty var barcode: String) extends AbstractResultDTO {
  def this() = this(null)
}
