package info.armado.ausleihe.client.transport.results

import info.armado.ausleihe.client.transport.dataobjects.entities._
import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class InformationDTO(
    var games: Array[GameDTO],
    var identityCard: IdentityCardDTO,
    var envelope: EnvelopeDTO
) extends AbstractResultDTO {

  def this() = this(Array.empty, null, null)

  override def equals(other: Any): Boolean = {
    val IdentityCard = identityCard
    val Envelope = envelope

    other match {
      case InformationDTO(otherGames, IdentityCard, Envelope) if games.sameElements(otherGames) =>
        true
      case _ => false
    }
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + (if (games == null) 0 else games.toSet.hashCode)
    result = prime * result + (if (identityCard == null) 0 else identityCard.hashCode)
    result = prime * result + (if (envelope == null) 0 else envelope.hashCode)

    return result
  }

  override def toString: String = s"Information(${games.toSet}, $identityCard, $envelope)"
}
