package info.armado.ausleihe.model.transport

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class GameDTO(var barcode: String,
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

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class DurationDTO(var min: Integer, var max: Integer) {
  def this() = this(null, null)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class PlayerCountDTO(var min: Integer, var max: Integer) {
  def this() = this(null, null)
}
