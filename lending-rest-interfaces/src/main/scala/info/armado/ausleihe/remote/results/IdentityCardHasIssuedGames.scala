package info.armado.ausleihe.remote.results

import info.armado.ausleihe.remote.dataobjects.entities.IdentityCardData
import javax.xml.bind.annotation.XmlRootElement
import info.armado.ausleihe.remote.dataobjects.entities.GameData
import info.armado.ausleihe.remote.util.Annotations._

@XmlRootElement
case class IdentityCardHasIssuedGames(
    @BeanProperty var identityCard: IdentityCardData,
    @BeanProperty var games: Array[GameData]) extends AbstractResult {

  def this() = this(null, Array.empty)

  override def equals(other: Any): Boolean = {
    val IdentityCard = identityCard

    other match {
      case IdentityCardHasIssuedGames(IdentityCard, otherGames) if games.sameElements(otherGames) => true
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

  override def toString: String = s"IdentityCardHasIssuedGames($identityCard, ${games.toSet})"
}