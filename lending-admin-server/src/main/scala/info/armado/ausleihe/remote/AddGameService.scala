package info.armado.ausleihe.remote

import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}

import info.armado.ausleihe.database.access.GamesDao
import info.armado.ausleihe.database.barcode.{InvalidBarcode, ValidBarcode, ValidateBarcode}
import info.armado.ausleihe.database.dataobjects.{GameDuration, PlayerCount}
import info.armado.ausleihe.database.entities.Game
import info.armado.ausleihe.model._

import scala.collection.JavaConverters.seqAsJavaListConverter

@Path("/games")
@RequestScoped
class AddGameService {

  @Inject
  var gamesDao: GamesDao = _

  @PUT
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Path("/add")
  @Transactional
  def addGame(game: GameDTO): Response = ValidateBarcode(game.barcode) match {
    case ValidBarcode(barcode) if !gamesDao.exists(barcode) => {
      val newGame = Game(barcode)

      newGame.title = game.title

      Option(game.author).foreach(author => newGame.author = author)
      Option(game.publisher).foreach(publisher => newGame.publisher = publisher)
      Option(game.comment).foreach(comment => newGame.comment = comment)
      Option(game.minAge).foreach(minAge => newGame.minimumAge = minAge)
      Option(game.playerCount).foreach(playerCount => newGame.playerCount = PlayerCount(playerCount.min, playerCount.max))
      Option(game.duration).foreach(duration => newGame.gameDuration = GameDuration(duration.min, duration.max))
      Option(game.activated).foreach(activated => newGame.available = activated)

      gamesDao.insert(newGame)

      var response = new AddGameResponseDTO()

      response.success = true
      response.responseMessage = s"Spiel ${barcode.toString} erfolgreich hinzugefügt"

      Response.ok(response).build()
    }
    case ValidBarcode(barcode) => {
      var response = new AddGameResponseDTO()

      response.success = false
      response.responseMessage = s"Spiel ${barcode.toString} existiert schon"

      Response.ok(response).build()
    }
    case InvalidBarcode(_) => Response.status(Response.Status.BAD_REQUEST).build()
  }

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/all")
  @Transactional
  def selectAllGames(): Response = Response.ok(
    gamesDao.selectAll().map(game => {
      val result = new GameDTO()

      result.barcode = game.barcode.toString
      result.title = game.title
      result.author = game.author
      result.publisher = game.publisher
      result.minAge = game.minimumAge

      Option(game.gameDuration).foreach(duration => result.duration = new DurationDTO(duration.minDuration, duration.maxDuration))
      Option(game.playerCount).foreach(playerCount => result.playerCount = new PlayerCountDTO(playerCount.minPlayerCount, playerCount.maxPlayerCount))

      result.activated = game.available

      result
    }).asJava).build()

  @PUT
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Path("/verify")
  @Transactional
  def verifyGames(games: Array[GameDTO]): Response = {
    val verifyGamesResponse = new VerifyGamesResponseDTO

    // find entries with wrong or already existing barcodes
    val alreadyExistingGameBarcodes = games.filter(game => ValidateBarcode(game.barcode) match {
      case ValidBarcode(barcode) => gamesDao.exists(barcode)
      case _ => true
    }).map(_.barcode)

    // find entries without set titles
    val gameBarcodesWithoutTitle = games.filter(game => Option(game.title) match {
      case Some("") | None => true
      case Some(_) => false
    }).map(_.barcode)

    verifyGamesResponse.alreadyExistingBarcodes = alreadyExistingGameBarcodes
    verifyGamesResponse.emptyTitleBarcodes = gameBarcodesWithoutTitle
    verifyGamesResponse.valid = alreadyExistingGameBarcodes.isEmpty && gameBarcodesWithoutTitle.isEmpty

    Response.ok(verifyGamesResponse).build()
  }
}
