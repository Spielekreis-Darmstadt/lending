package info.armado.ausleihe.remote.results

import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import info.armado.ausleihe.remote.dataobjects.entities._
import javax.xml.bind.annotation.XmlAccessType
import info.armado.ausleihe.remote.util.Annotations._

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class Information(
    @BeanProperty var games: Array[GameData],
    @BeanProperty var identityCard: IdentityCardData,
    @BeanProperty var envelope: EnvelopeData) extends AbstractResult {

  def this() = this(Array.empty, null, null)

  override def equals(other: Any): Boolean = {
    val IdentityCard = identityCard
    val Envelope = envelope

    other match {
      case Information(otherGames, IdentityCard, Envelope) if games.sameElements(otherGames) => true
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