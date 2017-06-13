package info.armado.ausleihe.remote.results

import info.armado.ausleihe.remote.dataobjects.entities._
import javax.xml.bind.annotation.XmlRootElement
import scala.collection.JavaConverters._
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlSeeAlso
import info.armado.ausleihe.remote.util.Annotations._

@XmlRootElement
case class IssueGamesSuccess(
    @BeanProperty var identityCard: IdentityCardData,
    @BeanProperty var games: Array[GameData]) extends AbstractResult {

  def this() = this(null, Array.empty)

  override def equals(other: Any): Boolean = {
    val IdentityCard = identityCard

    other match {
      case IssueGamesSuccess(IdentityCard, otherGames) if games.sameElements(otherGames) => true
      case _ => false
    }
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + (if (identityCard == null) 0 else identityCard.hashCode)
    result = prime * result + (if (games == null) 0 else games.toSet.hashCode)

    return result
  }

  override def toString: String = s"IssueGamesSuccess($identityCard, ${games.toSet})"
}