package info.armado.ausleihe.client.util

import java.time.{Duration, LocalDateTime}

import info.armado.ausleihe.database.entities._
import info.armado.ausleihe.remote.client.dataobjects.LendGameStatusDTO
import info.armado.ausleihe.remote.client.dataobjects.entities.{EnvelopeDTO, GameDTO, IdentityCardDTO}

object DTOExtensions {

  implicit class GameExtension(game: Game) {
    def toGameDTO: GameDTO = game match {
      case Game(barcode, title, author, publisher, playerCount, gameDuration, minimumAge, _, _, available) =>
        GameDTO(barcode.toString, title, author, publisher, Option(minimumAge).map {
          _.toString()
        }.orNull, Option(playerCount).map {
          _.toString()
        }.orNull, Option(gameDuration).map {
          _.toString()
        }.orNull)
    }
  }

  implicit class IdentityCardExtension(idCard: IdentityCard) {
    def toIdentityCardDTO: IdentityCardDTO = IdentityCardDTO(idCard.barcode.toString)
  }

  implicit class EnvelopeExtension(envelope: Envelope) {
    def toEnvelopeDTO: EnvelopeDTO = EnvelopeDTO(envelope.barcode.toString)
  }

  implicit class LendIdentityCardExtension(lendIdCard: LendIdentityCard) {
    def toIdentityCardDTO: IdentityCardDTO = IdentityCardDTO(lendIdCard.identityCard.barcode.toString, lendIdCard.owner)

    def toEnvelopeDTO: EnvelopeDTO = lendIdCard.envelope.toEnvelopeDTO

    def toGameDTO: Array[GameDTO] = lendIdCard.currentLendGames.map {
      _.toGameDTO
    }.toArray
  }

  implicit class LendGameExtension(lendGame: LendGame) {
    def toGameDTO: GameDTO = lendGame.game.toGameDTO

    def toIdentityCardDTO: IdentityCardDTO = lendGame.lendIdentityCard.toIdentityCardDTO

    def toEnvelopeDTO: EnvelopeDTO = lendGame.lendIdentityCard.toEnvelopeDTO
  }

  implicit class LendGameStatusDataExtension(game: Game) {
    def toLendGameStatusDTO(lendGame: Option[LendGame]): LendGameStatusDTO = lendGame match {
      case None => LendGameStatusDTO(game.toGameDTO, false, null)
      case Some(lendGame) => LendGameStatusDTO(game.toGameDTO, true, Duration.between(lendGame.getLendTime, LocalDateTime.now()))
    }
  }

}