package info.armado.ausleihe.remote.client.dataobjects.information

import info.armado.ausleihe.remote.client.dataobjects.LendGameStatusDTO
import info.armado.ausleihe.remote.client.requests.GameInformationRequestDTO
import info.armado.ausleihe.remote.client.util.Annotations._
import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class GameInformationDTO(@BeanProperty var request: GameInformationRequestDTO,
                              @BeanProperty var foundGames: Array[LendGameStatusDTO]) {

  def this() = this(null, Array.empty)

  override def equals(other: Any): Boolean = {
    val Request = request

    other match {
      case GameInformationDTO(Request, otherFoundGames) if foundGames.sameElements(otherFoundGames) => true
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