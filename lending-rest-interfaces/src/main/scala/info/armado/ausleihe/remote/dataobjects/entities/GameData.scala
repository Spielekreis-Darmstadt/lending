package info.armado.ausleihe.remote.dataobjects.entities

import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.bind.annotation.XmlAccessType
import info.armado.ausleihe.remote.util.Annotations._

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class GameData(
    @BeanProperty var barcode: String,
    @BeanProperty var title: String,
    @BeanProperty var author: String,
    @BeanProperty var publisher: String,
    @BeanProperty var mininumAge: String,
    @BeanProperty var playerCount: String,
    @BeanProperty var gameDuration: String) extends LendingEntity {
  
  def this() = this(null, null, null, null, null, null, null)
}