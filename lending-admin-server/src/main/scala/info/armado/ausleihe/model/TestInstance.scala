package info.armado.ausleihe.model

import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

import scala.beans.BeanProperty

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class TestInstance {
  @BeanProperty
  var name: String = _

  @BeanProperty
  var attribute: Int = _
}
