package info.armado.ausleihe.client.transport.dataobjects.entities

import info.armado.ausleihe.client.transport.util.Annotations._
import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class GameDTO(@BeanProperty var barcode: String,
                   @BeanProperty var title: String,
                   @BeanProperty var author: String,
                   @BeanProperty var publisher: String,
                   @BeanProperty var mininumAge: String,
                   @BeanProperty var playerCount: String,
                   @BeanProperty var gameDuration: String) extends LendingEntityDTO {

  def this() = this(null, null, null, null, null, null, null)
}