package info.armado.ausleihe.remote.client.dataobjects.inuse

import info.armado.ausleihe.remote.client.dataobjects.entities._
import info.armado.ausleihe.remote.client.util.Annotations._
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
case class IdentityCardInUseDTO(
                                 @BeanProperty var envelope: EnvelopeDTO,
                                 @BeanProperty var games: Array[GameDTO]) extends AbstractInUseInformationDTO {

  def this() = this(null, Array.empty)

  override def equals(other: Any): Boolean = {
    val Envelope = envelope

    other match {
      case IdentityCardInUseDTO(Envelope, otherGames) if games.sameElements(otherGames) => true
      case _ => false
    }
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + (if (envelope == null) 0 else envelope.hashCode)
    result = prime * result + (if (games == null) 0 else games.toSet.hashCode)

    return result
  }

  override def toString: String = s"IdentityCardInUse($envelope, ${games.toSet})"
}