package info.armado.ausleihe.admin.model

import javax.enterprise.context.RequestScoped
import javax.inject.Inject

import info.armado.ausleihe.database.access.{GamesDao, IdentityCardDao, LendGameDao, LendIdentityCardDao}
import org.apache.deltaspike.scheduler.api.Scheduled

@RequestScoped
@Scheduled(cronExpression = "0/3 * * * * ?")
class ClientUpdateScheduler extends Runnable {
  @Inject
  var gameDao: GamesDao = _

  @Inject
  var identityCardDao: IdentityCardDao = _

  @Inject
  var lendGameDao: LendGameDao = _

  @Inject
  var lendIdentityCardDao: LendIdentityCardDao = _

  @Inject
  var clients: Clients = _

  override def run(): Unit = clients.sendUpdate(createUpdate())

  private def createUpdate(): OverviewUpdate = {
    val totalGames = gameDao.selectAvailableNumber
    val totalIdentityCards = identityCardDao.selectAvailableNumber

    val lendGames = lendGameDao.selectNumberOfCurrentLendEntities
    val lendIdentityCards = lendIdentityCardDao.selectNumberOfCurrentLendEntities

    new OverviewUpdate(totalGames, lendGames, totalIdentityCards, lendIdentityCards)
  }
}
