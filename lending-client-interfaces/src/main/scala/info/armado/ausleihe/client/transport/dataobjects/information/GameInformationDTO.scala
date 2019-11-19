package info.armado.ausleihe.client.transport.dataobjects.information

import info.armado.ausleihe.client.transport.dataobjects.LendGameStatusDTO
import info.armado.ausleihe.client.transport.requests.GameInformationRequestDTO
import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class GameInformationDTO(
    var request: GameInformationRequestDTO,
    var foundGames: Array[LendGameStatusDTO]
) {

  def this() = this(null, Array.empty)

  override def equals(other: Any): Boolean = {
    val Request = request

    other match {
      case GameInformationDTO(Request, otherFoundGames)
          if foundGames.sameElements(otherFoundGames) =>
        true
      case _ => false
    }
  }

  override def hashCode: Int = {
    val prime = 31
    var result = 1

    result = prime * result + (if (request == null) 0 else request.hashCode)
    result = prime * result + (if (foundGames == null) 0 else foundGames.toSet.hashCode)

    result
  }

  override def toString: String = s"GameInformation($request, ${foundGames.toSet})"
}
