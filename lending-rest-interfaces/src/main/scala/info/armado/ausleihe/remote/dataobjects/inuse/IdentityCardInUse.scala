package info.armado.ausleihe.remote.dataobjects.inuse

import info.armado.ausleihe.remote.dataobjects.entities.EnvelopeData
import info.armado.ausleihe.remote.dataobjects.entities.GameData
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlSeeAlso
import info.armado.ausleihe.remote.util.Annotations._
import java.util.Arrays

@XmlRootElement
case class IdentityCardInUse(
  @BeanProperty var envelope: EnvelopeData,  
  @BeanProperty var games: Array[GameData]) extends AbstractInUseInformation {
  
  def this() = this(null, Array.empty)
  
  override def equals(other: Any): Boolean = {
    val Envelope = envelope
    
    other match {
      case IdentityCardInUse(Envelope, otherGames) if games.sameElements(otherGames) => true
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