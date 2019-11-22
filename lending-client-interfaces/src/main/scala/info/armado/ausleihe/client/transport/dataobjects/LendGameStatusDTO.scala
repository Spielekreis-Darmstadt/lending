package info.armado.ausleihe.client.transport.dataobjects

import java.time.Duration

import info.armado.ausleihe.client.transport.converter.DurationAdapter
import info.armado.ausleihe.client.transport.util.Annotations._
import info.armado.ausleihe.client.transport.dataobjects.entities._
import javax.xml.bind.annotation.{XmlAccessType, XmlAccessorType, XmlRootElement}

object LendGameStatusDTO {

  /**
    * Create a [[LendGameStatusDTO]] instance for an unlend [[GameDTO]] object
    *
    * @param game The unlend game
    * @return The created [[LendGameStatusDTO]] instance
    */
  def apply(game: GameDTO): LendGameStatusDTO =
    LendGameStatusDTO(game, false, null, null)

  /**
    * Create a [[LendGameStatusDTO]] instance for a lend [[GameDTO]] object
    *
    * @param game         The unlend game
    * @param lendDuration The current lend duration (time between lend start time until now)
    * @param owner        The owner of the identity card if set
    * @return The created [[LendGameStatusDTO]] instance
    */
  def apply(game: GameDTO, lendDuration: Duration, owner: String): LendGameStatusDTO =
    LendGameStatusDTO(game, true, lendDuration, owner)
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
case class LendGameStatusDTO(
    var game: GameDTO,
    var lend: Boolean,
    @XmlJavaTypeAdapter(value = classOf[DurationAdapter])
    var lendDuration: Duration,
    var owner: String
) {

  def this() = this(null, false, null, null)
}
