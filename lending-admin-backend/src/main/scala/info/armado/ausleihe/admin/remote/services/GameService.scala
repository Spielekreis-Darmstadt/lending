package info.armado.ausleihe.admin.remote.services

import info.armado.ausleihe.admin.transport.dataobjects.GameDTO
import info.armado.ausleihe.admin.transport.responses.{AddGamesResponseDTO, VerifyGamesResponseDTO}
import info.armado.ausleihe.admin.util.DTOExtensions.{GameDTOExtension, GameExtension}
import info.armado.ausleihe.database.access.GamesDao
import info.armado.ausleihe.database.barcode.{Barcode, ValidBarcode, ValidateBarcode}
import info.armado.ausleihe.database.dataobjects.Prefix
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}

@Path("/games")
@RequestScoped
class GameService {

  @Inject
  var gamesDao: GamesDao = _

  /**
    * Adds a list of games to the database.
    * Before the games are added the input data is verified and validated.
    * If at least one input entry is invalid, because it for example doesn't contain a title
    * or its barcode is already contained in the database, the whole method terminates and no
    * game is added to the database.
    * In both the successful and the unsuccessful cases, an [[AddGamesResponseDTO]] object is returned
    *
    * @param gameDtos The games to be added
    * @return A response object, containing the result of the operation
    */
  @PUT
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Path("/add")
  @Transactional
  def addGames(gameDtos: Array[GameDTO]): AddGamesResponseDTO = verify(gameDtos) match {
    case VerifyGamesResponseDTO(true, Array(), Array(), Array()) => {
      // convert GameDTO objects to Game objects
      val games = gameDtos.map(_.toGame)

      // insert the Game objects into the database
      gamesDao.insert(games)

      // send success result to the client
      AddGamesResponseDTO(true)
    }
    case VerifyGamesResponseDTO(false, alreadyExistingBarcodes, duplicateBarcodes, emptyTitleBarcodes) => {
      // the given games information are not valid
      AddGamesResponseDTO(alreadyExistingBarcodes, duplicateBarcodes, emptyTitleBarcodes)
    }
    case _ => throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
  }

  private def verify(gameDtos: Array[GameDTO]): VerifyGamesResponseDTO = {
    // find entries with wrong or already existing barcodes
    val alreadyExistingGameBarcodes = gameDtos.filter(gameDto => ValidateBarcode(gameDto.barcode) match {
      case ValidBarcode(barcode@Barcode(Prefix.Spielekreis | Prefix.BDKJ, _, _)) => gamesDao.exists(barcode)
      case _ => true
    }).map(_.barcode)

    // find entries without set titles
    val gameBarcodesWithoutTitle = gameDtos.filter(game => Option(game.title) match {
      case Some("") | None => true
      case Some(_) => false
    }).map(_.barcode)

    // find all duplicate barcodes
    val duplicateGameBarcodes = gameDtos
      .map(_.barcode).groupBy(identity).collect { case (x, Array(_, _, _*)) => x }.toArray

    VerifyGamesResponseDTO(alreadyExistingGameBarcodes, duplicateGameBarcodes, gameBarcodesWithoutTitle)
  }

  /**
    * Selects all games from the database
    *
    * @return An array containing all games inside the database
    */
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/all")
  @Transactional
  def selectAllGames(): Array[GameDTO] =
    gamesDao.selectAll().map(_.toGameDTO).toArray

  /**
    * Selects all games from the database, that have a barcode, which is contained in the given `barcodes` array.
    * If no game can be found for a given barcode, the barcode is ignored
    *
    * @param barcodes An array containing the barcodes of all queried games
    * @return An array containing the found games belonging to the given barcodes
    */
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/select")
  @Transactional
  def selectGames(barcodes: Array[String]): Array[GameDTO] =
    barcodes.flatMap(barcode => ValidateBarcode(barcode) match {
      case ValidBarcode(barcode@Barcode(Prefix.Spielekreis | Prefix.BDKJ, _, _)) => gamesDao.selectByBarcode(barcode)
      case _ => None
    }).map(_.toGameDTO)

  /**
    * Verifies a given list of games, if they:
    * - don't already exist in the database
    * - contain a title
    *
    * @param games The games information to be checked
    * @return A response wrapping a [[VerifyGamesResponseDTO]] object
    */
  @PUT
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Path("/verify")
  @Transactional
  def verifyGames(games: Array[GameDTO]): VerifyGamesResponseDTO = verify(games)
}
