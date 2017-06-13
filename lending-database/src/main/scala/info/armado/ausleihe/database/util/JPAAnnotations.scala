package info.armado.ausleihe.database.util

import scala.annotation.meta.field

object JPAAnnotations {
  type BeanProperty = scala.beans.BeanProperty @field
  type Id = javax.persistence.Id @field
  type Column = javax.persistence.Column @field
  type GeneratedValue = javax.persistence.GeneratedValue @field
  type Embedded = javax.persistence.Embedded @field
  type OneToMany = javax.persistence.OneToMany @field
}