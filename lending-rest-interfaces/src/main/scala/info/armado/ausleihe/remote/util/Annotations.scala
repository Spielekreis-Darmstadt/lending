package info.armado.ausleihe.remote.util

import scala.annotation.meta.field

object Annotations {
  type BeanProperty = scala.beans.BeanProperty @field
  type XmlJavaTypeAdapter = javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter @field
}