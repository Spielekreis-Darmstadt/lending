package info.armado.ausleihe.remote.results

import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlSeeAlso
import info.armado.ausleihe.remote.util.Annotations._

@XmlRootElement
case class IncorrectBarcode(@BeanProperty var barcode: String) extends AbstractResult {
  def this() = this(null)
}