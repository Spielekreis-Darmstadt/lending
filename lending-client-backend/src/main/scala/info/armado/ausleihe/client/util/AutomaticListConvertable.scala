package info.armado.ausleihe.client.util

import java.util.{List => JList}
import scala.collection.JavaConverters._

trait AutomaticListConvertable {
  implicit def toScalaList[E](list: JList[E]): List[E] = list.asScala.toList
  implicit def toJavaList[E](list: List[E]): JList[E] = list.asJava
}