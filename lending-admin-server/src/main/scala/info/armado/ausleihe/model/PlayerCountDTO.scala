package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class PlayerCountDTO {
  var min: Integer = _

  var max: Integer = _
}
