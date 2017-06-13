package info.armado.ausleihe.remote.dataobjects

import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlAccessType
import java.time.Duration
import info.armado.ausleihe.remote.converter.DurationAdapter
import info.armado.ausleihe.remote.dataobjects.entities.GameData
import info.armado.ausleihe.remote.util.Annotations._

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class LendGameStatusData( 
  @BeanProperty var game: GameData,  
  @BeanProperty var lend: Boolean,  
  @BeanProperty @XmlJavaTypeAdapter(value = classOf[DurationAdapter]) var lendDuration: Duration) {
  
  def this() = this(null, false, null)
}