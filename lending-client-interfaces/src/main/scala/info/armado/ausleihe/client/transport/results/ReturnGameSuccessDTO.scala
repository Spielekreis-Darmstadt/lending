package info.armado.ausleihe.client.transport.results

import info.armado.ausleihe.client.transport.dataobjects.entities.GameDTO
import info.armado.ausleihe.client.transport.util.Annotations._
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class ReturnGameSuccessDTO(@BeanProperty var game: GameDTO) extends AbstractResultDTO {
  def this() = this(null)
}
