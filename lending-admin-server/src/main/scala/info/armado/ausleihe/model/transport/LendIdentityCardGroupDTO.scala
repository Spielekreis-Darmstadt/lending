package info.armado.ausleihe.model.transport

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class LendIdentityCardGroupDTO(var barcode: String,
                               var lendGames: Array[LendGameDTO]) {
  def this() = this(null, Array())
}
