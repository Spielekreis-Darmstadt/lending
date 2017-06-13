package info.armado.ausleihe.remote.results

import info.armado.ausleihe.remote.dataobjects.entities.GameData
import javax.xml.bind.annotation.XmlRootElement
import info.armado.ausleihe.remote.util.Annotations._

@XmlRootElement
case class ReturnGameSuccess(@BeanProperty var game: GameData) extends AbstractResult {
  def this() = this(null)
}