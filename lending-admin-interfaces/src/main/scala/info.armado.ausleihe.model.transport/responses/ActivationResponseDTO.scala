package info.armado.ausleihe.model.transport.responses

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class ActivationResponseDTO(var correctBarcodes: Array[String],
                                 var incorrectBarcodes: Array[String]) {
  def this() = this(Array(), Array())
}
