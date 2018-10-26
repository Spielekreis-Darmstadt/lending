package info.armado.ausleihe.model.transport.dataobjects

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class LendIdentityCardGroupDTO(var barcode: String,
                                    var lendGames: Array[LendGameDTO]) {
  def this() = this(null, Array())
}
