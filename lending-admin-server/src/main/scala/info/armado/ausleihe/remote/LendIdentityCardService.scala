package info.armado.ausleihe.remote

import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.core.{MediaType, Response}
import javax.ws.rs.{GET, Path, Produces}

import info.armado.ausleihe.database.access.LendIdentityCardDao
import info.armado.ausleihe.database.entities.LendIdentityCard
import info.armado.ausleihe.model.transport.LendIdentityCardDTO

@Path("/lend/identity-cards")
@RequestScoped
class LendIdentityCardService {
  @Inject
  var lendIdentityCardDao: LendIdentityCardDao = _

  /**
    * Select all currently issued identity cards
    *
    * @return An array containing all currently issued identity cards
    */
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/all")
  @Transactional
  def selectAllLendIdentityCards(): Response = Response.ok(
    lendIdentityCardDao.selectAllCurrentLend
      .map(lendIdentityCard => toLendIdentityCardDTO(lendIdentityCard)).toArray
  ).build()

  def toLendIdentityCardDTO(lendIdentityCard: LendIdentityCard): LendIdentityCardDTO = {
    val result = new LendIdentityCardDTO()

    result.identityCardBarcode = lendIdentityCard.identityCard.barcode.toString
    result.envelopeBarcode = lendIdentityCard.envelope.barcode.toString
    result.lendTime = lendIdentityCard.lendTime.toString
    result.numberOfLendGames = lendIdentityCard.currentLendGames.length

    result
  }
}
