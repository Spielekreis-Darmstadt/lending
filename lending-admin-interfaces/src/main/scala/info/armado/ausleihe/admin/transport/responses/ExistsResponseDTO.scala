package info.armado.ausleihe.admin.transport.responses

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class ExistsResponseDTO(var input: String,
                             var exists: Boolean) {
  def this() = this(null, false)
}
