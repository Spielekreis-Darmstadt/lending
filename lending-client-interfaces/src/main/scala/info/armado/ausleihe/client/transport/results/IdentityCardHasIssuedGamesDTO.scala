package info.armado.ausleihe.client.transport.results

import info.armado.ausleihe.client.transport.dataobjects.entities._
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class IdentityCardHasIssuedGamesDTO(var identityCard: IdentityCardDTO,
                                         var games: Array[GameDTO]) extends AbstractResultDTO {

  def this() = this(null, Array.empty)

  override def equals(other: Any): Boolean = {
    val IdentityCard = identityCard

    other match {
      case IdentityCardHasIssuedGamesDTO(IdentityCard, otherGames) if games.sameElements(otherGames) => true
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