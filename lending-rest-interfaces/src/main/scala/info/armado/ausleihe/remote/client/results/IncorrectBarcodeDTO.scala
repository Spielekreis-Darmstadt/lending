package info.armado.ausleihe.remote.client.results

import info.armado.ausleihe.remote.client.util.Annotations._
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class IncorrectBarcodeDTO(@BeanProperty var barcode: String) extends AbstractResultDTO {
  def this() = this(null)
}
