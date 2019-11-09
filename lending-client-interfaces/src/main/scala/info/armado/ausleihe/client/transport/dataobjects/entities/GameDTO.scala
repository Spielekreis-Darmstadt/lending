package info.armado.ausleihe.client.transport.dataobjects.entities

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class GameDTO(var barcode: String,
                   var title: String,
                   var author: String,
                   var publisher: String,
                   var mininumAge: String,
                   var playerCount: String,
                   var gameDuration: String,
                   var releaseYear: Integer) extends LendingEntityDTO {

  def this() = this(null, null, null, null, null, null, null, null)
}