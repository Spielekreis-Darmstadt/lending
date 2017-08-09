package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class GameDTO {
  var barcode: String = _

  var title: String = _

  var author: String = _

  var publisher: String = _

  var minAge: Integer = _

  var playerCount: PlayerCountDTO = _

  var duration: DurationDTO = _

  var comment: String = _
}
