package info.armado.ausleihe.remote.client.results

import info.armado.ausleihe.remote.client.dataobjects.entities.GameDTO
import info.armado.ausleihe.remote.client.util.Annotations._
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class ReturnGameSuccessDTO(@BeanProperty var game: GameDTO) extends AbstractResultDTO {
  def this() = this(null)
}
