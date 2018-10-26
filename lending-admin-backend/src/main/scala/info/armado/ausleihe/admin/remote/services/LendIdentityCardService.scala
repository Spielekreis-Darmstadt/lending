package info.armado.ausleihe.admin.remote.services

import info.armado.ausleihe.admin.transport.dataobjects.LendIdentityCardDTO
import info.armado.ausleihe.admin.transport.requests.ChangeOwnerRequestDTO
import info.armado.ausleihe.admin.util.DTOExtensions.LendIdentityCardExtension
import info.armado.ausleihe.database.access.LendIdentityCardDao
import info.armado.ausleihe.database.barcode.{InvalidBarcode, ValidBarcode, ValidateBarcode}
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.MediaType

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
  def selectAllLendIdentityCards(): Array[LendIdentityCardDTO] =
    lendIdentityCardDao.selectAllCurrentLend.map(_.toLendIdentityCardDTO).toArray

  /**
    * Updates the owner of an issued identity card
    *
    * @param changeOwnerRequest The request containing the barcode of the issued identity card and the new owner
    * @return The updated lend identity card
    */
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/owner")
  @Transactional
  def updateOwner(changeOwnerRequest: ChangeOwnerRequestDTO): LendIdentityCardDTO = changeOwnerRequest match {
    case ChangeOwnerRequestDTO(identityCardBarcodeString, owner) => ValidateBarcode(identityCardBarcodeString) match {
      case ValidBarcode(identityCardBarcode) => lendIdentityCardDao.selectCurrentByIdentityCardBarcode(identityCardBarcode) match {
        case Some(lendIdentityCard) => {
          lendIdentityCardDao.updateOwner(lendIdentityCard, owner)

          lendIdentityCard.toLendIdentityCardDTO
        }
        case None => throw new NotFoundException("Non issued identity card");
      }
      case InvalidBarcode(_) => throw new BadRequestException("Invalid identity card barcode");
    }
  }
}
