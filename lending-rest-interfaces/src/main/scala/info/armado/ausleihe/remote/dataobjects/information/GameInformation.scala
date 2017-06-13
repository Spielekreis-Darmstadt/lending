package info.armado.ausleihe.remote.dataobjects.information

import info.armado.ausleihe.remote.dataobjects.LendGameStatusData
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlAccessType
import info.armado.ausleihe.remote.requests.GameInformationRequest
import info.armado.ausleihe.remote.util.Annotations._

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class GameInformation(
    @BeanProperty var request: GameInformationRequest,
    @BeanProperty var foundGames: Array[LendGameStatusData]) {

  def this() = this(null, Array.empty)

  override def equals(other: Any): Boolean = {
    val Request = request

    other match {
      case GameInformation(Request, otherFoundGames) if foundGames.sameElements(otherFoundGames) => true
      case _ => false
    }
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + (if (request == null) 0 else request.hashCode)
    result = prime * result + (if (foundGames == null) 0 else foundGames.toSet.hashCode)

    return result
  }

  override def toString: String = s"GameInformation($request, ${foundGames.toSet})"
}