package info.armado.ausleihe.admin.util

import info.armado.ausleihe.database.barcode.Barcode
import info.armado.ausleihe.database.dataobjects.{GameDuration, PlayerCount}
import info.armado.ausleihe.database.entities._
import info.armado.ausleihe.admin.transport.dataobjects._
import java.time.Year

object DTOExtensions {

  implicit class EnvelopeExtension(envelope: Envelope) {
    def toEnvelopeDTO: EnvelopeDTO = {
      val result = new EnvelopeDTO

      result.barcode = envelope.barcode.toString
      result.activated = envelope.available

      result
    }
  }

  implicit class EnvelopeDTOExtension(envelope: EnvelopeDTO) {
    def toEnvelope: Envelope = {
      val result = Envelope(Barcode(envelope.barcode))

      Option(envelope.activated).foreach(activated => result.available = activated)

      result
    }
  }

  implicit class GameExtension(game: Game) {
    def toGameDTO: GameDTO = {
      val result = new GameDTO

      result.barcode = game.barcode.toString
      result.title = game.title
      result.author = game.author
      result.publisher = game.publisher
      result.minAge = game.minimumAge

      Option(game.gameDuration).foreach(duration => result.duration = DurationDTO(duration.minDuration, duration.maxDuration))
      Option(game.playerCount).foreach(playerCount => result.playerCount = PlayerCountDTO(playerCount.minPlayerCount, playerCount.maxPlayerCount))

      Option(game.releaseYear).foreach(releaseYear => result.releaseYear = releaseYear.getValue)
      result.activated = game.available

      result
    }
  }

  implicit class GameDTOExtension(game: GameDTO) {
    def toGame: Game = {
      val newGame = Game(Barcode(game.barcode))

      newGame.title = game.title

      Option(game.author).foreach(author => newGame.author = author)
      Option(game.publisher).foreach(publisher => newGame.publisher = publisher)
      Option(game.comment).foreach(comment => newGame.comment = comment)
      Option(game.minAge).foreach(minAge => newGame.minimumAge = minAge)
      Option(game.playerCount).foreach(playerCount => newGame.playerCount = PlayerCount(playerCount.min, playerCount.max))
      Option(game.duration).foreach(duration => newGame.gameDuration = GameDuration(duration.min, duration.max))
      Option(game.releaseYear).foreach(releaseYear => newGame.releaseYear = Year.of(releaseYear))
      Option(game.activated).foreach(activated => newGame.available = activated)

      newGame
    }
  }

  implicit class IdentityCardExtension(identityCard: IdentityCard) {
    def toIdentityCardDTO: IdentityCardDTO = {
      val result = new IdentityCardDTO

      result.barcode = identityCard.barcode.toString
      result.activated = identityCard.available

      result
    }
  }

  implicit class IdentityCardDTOExtension(identityCard: IdentityCardDTO) {
    def toIdentityCard: IdentityCard = {
      val result = IdentityCard(Barcode(identityCard.barcode))

      Option(identityCard.activated).foreach(activated => result.available = activated)

      result
    }
  }

  implicit class LendGameExtension(lendGame: LendGame) {
    def toLendGameDTO: LendGameDTO = {
      val result = new LendGameDTO

      result.barcode = lendGame.game.barcode.toString
      result.lendTime = lendGame.lendTime.toString

      result
    }
  }

  implicit class LendIdentityCardExtension(lendIdentityCard: LendIdentityCard) {
    def toLendIdentityCardGroupDTO: LendIdentityCardGroupDTO = {
      val result = new LendIdentityCardGroupDTO

      result.barcode = lendIdentityCard.identityCard.barcode.toString
      result.lendGames = lendIdentityCard.currentLendGames.map(_.toLendGameDTO).toArray

      result
    }

    def toLendIdentityCardDTO: LendIdentityCardDTO = {
      val result = new LendIdentityCardDTO

      result.identityCardBarcode = lendIdentityCard.identityCard.barcode.toString
      result.envelopeBarcode = lendIdentityCard.envelope.barcode.toString
      result.lendTime = lendIdentityCard.lendTime.toString
      result.numberOfLendGames = lendIdentityCard.currentLendGames.length
      result.owner = lendIdentityCard.owner

      result
    }
  }

}
