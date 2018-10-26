package info.armado.ausleihe.client.transport.results

import info.armado.ausleihe.client.transport.dataobjects.entities._
import info.armado.ausleihe.client.transport.util.Annotations._
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class IssueGamesSuccessDTO(@BeanProperty var identityCard: IdentityCardDTO,
                                @BeanProperty var games: Array[GameDTO]) extends AbstractResultDTO {

  def this() = this(null, Array.empty)

  override def equals(other: Any): Boolean = {
    val IdentityCard = identityCard

    other match {
      case IssueGamesSuccessDTO(IdentityCard, otherGames) if games.sameElements(otherGames) => true
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
