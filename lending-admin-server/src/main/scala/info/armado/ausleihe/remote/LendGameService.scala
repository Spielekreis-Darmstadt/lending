package info.armado.ausleihe.remote

import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.{GET, Path, Produces}
import javax.ws.rs.core.{MediaType, Response}

import info.armado.ausleihe.database.access.LendIdentityCardDao
import info.armado.ausleihe.database.entities.{LendGame, LendIdentityCard}
import info.armado.ausleihe.model.transport.{LendGameDTO, LendIdentityCardGroupDTO}

@Path("/lend/games")
@RequestScoped
class LendGameService {
  @Inject
  var lendIdentityCardDao: LendIdentityCardDao = _

  /**
    * Select all currently borrowed games, grouped by their identity card
    *
    * @return An array containing all borrowed game groups
    */
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/all")
  @Transactional
  def selectAllLendGames(): Response = Response.ok(
    lendIdentityCardDao.selectAllCurrentLend
      // remove all lend identity cards that have no lend game currently
      .filter(_.hasCurrentLendGames)
      .map(lendIdentityCard => toLendIdentityCardGroupDTO(lendIdentityCard)).toArray
  ).build()

  def toLendIdentityCardGroupDTO(lendIdentityCard: LendIdentityCard): LendIdentityCardGroupDTO = {
    val result = new LendIdentityCardGroupDTO()

    result.barcode = lendIdentityCard.identityCard.barcode.toString
    result.lendGames = lendIdentityCard.currentLendGames.map(lendGame => toLendGameDTO(lendGame)).toArray

    result
  }

  def toLendGameDTO(lendGame: LendGame): LendGameDTO = {
    val result = new LendGameDTO()

    result.barcode = lendGame.game.barcode.toString
    result.lendTime = lendGame.lendTime.toString

    result
  }
}
