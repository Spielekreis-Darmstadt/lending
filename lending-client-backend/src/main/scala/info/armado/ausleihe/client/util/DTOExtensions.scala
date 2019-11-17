package info.armado.ausleihe.client.util

import java.time.{Duration, LocalDateTime}

import info.armado.ausleihe.client.transport.dataobjects.LendGameStatusDTO
import info.armado.ausleihe.client.transport.dataobjects.entities._
import info.armado.ausleihe.database.entities._

object DTOExtensions {

  implicit class GameExtension(game: Game) {
    def toGameDTO: GameDTO = game match {
      case game: Game =>
        GameDTO(
          game.barcode.toString,
          game.title,
          game.author,
          game.publisher,
          Option(game.minimumAge).map(_.toString).orNull,
          Option(game.playerCount).map(_.toString).orNull,
          Option(game.gameDuration).map(_.toString).orNull,
          Option(game.releaseYear).map(year => Integer.valueOf(year.getValue)).orNull
        )
    }
  }

  implicit class IdentityCardExtension(idCard: IdentityCard) {
    def toIdentityCardDTO: IdentityCardDTO = IdentityCardDTO(idCard.barcode.toString)
  }

  implicit class EnvelopeExtension(envelope: Envelope) {
    def toEnvelopeDTO: EnvelopeDTO = EnvelopeDTO(envelope.barcode.toString)
  }

  implicit class LendIdentityCardExtension(lendIdCard: LendIdentityCard) {
    def toIdentityCardDTO: IdentityCardDTO =
      IdentityCardDTO(lendIdCard.identityCard.barcode.toString, lendIdCard.owner)

    def toEnvelopeDTO: EnvelopeDTO = lendIdCard.envelope.toEnvelopeDTO

    def toGameDTO: Array[GameDTO] = lendIdCard.currentLendGames.map(_.toGameDTO).toArray
  }

  implicit class LendGameExtension(lendGame: LendGame) {
    def toGameDTO: GameDTO = lendGame.game.toGameDTO

    def toIdentityCardDTO: IdentityCardDTO = lendGame.lendIdentityCard.toIdentityCardDTO

    def toEnvelopeDTO: EnvelopeDTO = lendGame.lendIdentityCard.toEnvelopeDTO
  }

  implicit class LendGameStatusDataExtension(game: Game) {
    def toLendGameStatusDTO(lendGame: Option[LendGame]): LendGameStatusDTO = lendGame match {
      case None => LendGameStatusDTO(game.toGameDTO)

      case Some(LendGame(_, lendIdentityCard, lendTime, _)) =>
        LendGameStatusDTO(
          game.toGameDTO,
          Duration.between(lendTime, LocalDateTime.now),
          lendIdentityCard.owner
        )
    }
  }

}
