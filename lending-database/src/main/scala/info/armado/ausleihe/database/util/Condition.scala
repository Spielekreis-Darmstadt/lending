package info.armado.ausleihe.database.util

import scala.collection.mutable.MutableList

trait Condition {
  def isEmpty: Boolean

  def toQueryString: String
}

case class StringCondition(condition: String) extends Condition {
  override def isEmpty: Boolean = false

  override def toQueryString: String = condition
}

case class AndCondition(conditions: MutableList[Condition]) extends Condition {
  def this() = this(MutableList.empty)

  def +(condition: Condition): Unit = conditions += condition

  override def isEmpty: Boolean = conditions.isEmpty

  override def toQueryString: String = conditions.map(condition => condition.toQueryString).mkString("(", " and ", ")")
}

case class OrCondition(conditions: MutableList[Condition]) extends Condition {
  def this() = this(MutableList.empty)

  def +(condition: Condition): Unit = conditions += condition

  override def isEmpty: Boolean = conditions.isEmpty

  override def toQueryString: String = conditions.map(condition => condition.toQueryString).mkString("(", " or ", ")")
}