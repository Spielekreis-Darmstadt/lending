package info.armado.ausleihe.client.transport.dataobjects.entities

import java.time.Year
import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}
import info.armado.ausleihe.client.transport.converter.YearAdapter
import info.armado.ausleihe.client.transport.util.Annotations._

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class GameDTO(
    var barcode: String,
    var title: String,
    var author: String,
    var publisher: String,
    var mininumAge: Integer,
    var playerCount: PlayerCountDTO,
    var gameDuration: DurationDTO,
    @XmlJavaTypeAdapter(value = classOf[YearAdapter])
    var releaseYear: Year
) extends LendingEntityDTO {

  def this() = this(null, null, null, null, null, null, null, null)
}
