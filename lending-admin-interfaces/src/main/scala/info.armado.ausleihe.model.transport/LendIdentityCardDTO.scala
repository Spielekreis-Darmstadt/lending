package info.armado.ausleihe.model.transport

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class LendIdentityCardDTO(var identityCardBarcode: String,
                          var envelopeBarcode: String,
                          var lendTime: String,
                          var numberOfLendGames: Int,
                          var owner: String) {
  def this() = this(null, null, null, 0, null)
}
