package info.armado.ausleihe.admin.remote

import info.armado.ausleihe.database.access.LendIdentityCardDao
import info.armado.ausleihe.admin.transport.dataobjects.LendIdentityCardGroupDTO
import info.armado.ausleihe.admin.util.DTOExtensions.LendIdentityCardExtension
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.core.MediaType
import javax.ws.rs.{GET, Path, Produces}

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
  def selectAllLendGames(): Array[LendIdentityCardGroupDTO] =
    lendIdentityCardDao.selectAllCurrentLend
      // remove all lend identity cards that have no lend game currently
      .filter(_.hasCurrentLendGames)
      .map(_.toLendIdentityCardGroupDTO).toArray
}
