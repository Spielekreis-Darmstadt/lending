package info.armado.ausleihe.model.transport.dataobjects

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class GameDTO(var barcode: String,
                   var title: String,
                   var author: String,
                   var publisher: String,
                   var minAge: Integer,
                   var playerCount: PlayerCountDTO,
                   var duration: DurationDTO,
                   var comment: String,
                   var activated: Boolean) {
  def this() = this(null, null, null, null, null, null, null, null, false)
}