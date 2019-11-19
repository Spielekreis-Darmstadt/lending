package info.armado.ausleihe.client.transport.dataobjects.entities

import java.time.Year
import info.armado.ausleihe.client.transport.converter.YearAdapter
import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlJavaTypeAdapter(value = classOf[YearAdapter])
case class GameDTO(
    var barcode: String,
    var title: String,
    var author: String,
    var publisher: String,
    var mininumAge: Year,
    var playerCount: PlayerCountDTO,
    var gameDuration: DurationDTO,
    var releaseYear: Year
) extends LendingEntityDTO {

  def this() = this(null, null, null, null, null, null, null, null)
}
