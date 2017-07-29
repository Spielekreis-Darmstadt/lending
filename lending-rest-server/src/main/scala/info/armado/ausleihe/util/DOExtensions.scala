package info.armado.ausleihe.util

import java.time.{Duration, LocalDateTime}

import info.armado.ausleihe.database.barcode._
import info.armado.ausleihe.database.entities._
import info.armado.ausleihe.remote.dataobjects.LendGameStatusData
import info.armado.ausleihe.remote.dataobjects.entities.{EnvelopeData, GameData, IdentityCardData}

object DOExtensions {
  implicit class GameExtension(game: Game) {
    def toGameData: GameData = game match {
      case Game(barcode, title, author, publisher, playerCount, gameDuration, minimumAge, _, _, available) =>
        GameData(barcode.toString, title, author, publisher, Option(minimumAge).map { _.toString() }.orNull, Option(playerCount).map { _.toString() }.orNull, Option(gameDuration).map { _.toString() }.orNull)
    }
  }

  implicit class IdentityCardExtension(idCard: IdentityCard) {
    def toIdentityCardData: IdentityCardData = IdentityCardData(idCard.barcode.toString)
  }

  implicit class EnvelopeExtension(envelope: Envelope) {
    def toEnvelopeData: EnvelopeData = EnvelopeData(envelope.barcode.toString)
  }

  implicit class LendIdentityCardExtension(lendIdCard: LendIdentityCard) {    
    def toIdentityCardData: IdentityCardData = IdentityCardData(lendIdCard.identityCard.barcode.toString, lendIdCard.owner)
    def toEnvelopeData: EnvelopeData = lendIdCard.envelope.toEnvelopeData
    def toGameData: Array[GameData] = lendIdCard.currentLendGames.map { _.toGameData }.toArray
  }

  implicit class LendGameExtension(lendGame: LendGame) {    
    def toGameData: GameData = lendGame.game.toGameData
    def toIdentityCardData: IdentityCardData = lendGame.lendIdentityCard.toIdentityCardData
    def toEnvelopeData: EnvelopeData = lendGame.lendIdentityCard.toEnvelopeData
  }

  implicit class LendGameStatusDataExtension(game: Game) {
    def toLendGameStatusData(lendGame: Option[LendGame]): LendGameStatusData = lendGame match {
      case None => LendGameStatusData(game.toGameData, false, null)
      case Some(lendGame) => LendGameStatusData(game.toGameData, true, Duration.between(lendGame.getLendTime(), LocalDateTime.now()))
    }
  }
  
  implicit def barcodeToString(barcode: Barcode): String = barcode.toString
}