package info.armado.ausleihe.util

import info.armado.ausleihe.database.dataobjects.IdentityCard
import info.armado.ausleihe.remote.dataobjects.entities.GameData
import info.armado.ausleihe.remote.dataobjects.entities.IdentityCardData
import info.armado.ausleihe.remote.dataobjects.entities.EnvelopeData
import java.time.LocalDateTime
import info.armado.ausleihe.database.dataobjects.LendGame
import info.armado.ausleihe.database.dataobjects.Game
import info.armado.ausleihe.database.dataobjects.LendIdentityCard
import info.armado.ausleihe.database.dataobjects.Envelope
import info.armado.ausleihe.remote.dataobjects.LendGameStatusData
import java.time.Duration
import info.armado.ausleihe.database.dataobjects.Barcode

object DOExtensions {
  implicit class GameExtension(game: Game) {
    def toGameData: GameData = game match {
      case Game(barcode, title, author, publisher, playerCount, gameDuration, minimumAge, _, _, available, _) =>
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
    def toLendGameStatusData(lendGame: LendGame): LendGameStatusData = Option(lendGame) match {
      case None => LendGameStatusData(game.toGameData, false, null)
      case Some(lendGame) => LendGameStatusData(game.toGameData, true, Duration.between(lendGame.getLendTime(), LocalDateTime.now()))
    }
  }
  
  implicit def barcodeToString(barcode: Barcode): String = barcode.toString
}