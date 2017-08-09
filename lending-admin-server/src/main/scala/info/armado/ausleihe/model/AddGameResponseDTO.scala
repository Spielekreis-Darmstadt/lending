package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class AddGameResponseDTO {
  var success: Boolean = _

  var responseMessage: String = _
}
