package info.armado.ausleihe.admin.remote

import info.armado.ausleihe.database.access.IdentityCardDao
import info.armado.ausleihe.database.barcode.{Barcode, ValidBarcode, ValidateBarcode}
import info.armado.ausleihe.database.dataobjects.Prefix
import info.armado.ausleihe.model.transport.dataobjects.IdentityCardDTO
import info.armado.ausleihe.model.transport.responses.{AddIdentityCardsResponseDTO, VerifyIdentityCardsResponseDTO}
import info.armado.ausleihe.admin.util.DTOExtensions.{IdentityCardDTOExtension, IdentityCardExtension}
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}

@Path("/identity-cards")
@RequestScoped
class IdentityCardService {
  @Inject
  var identityCardDao: IdentityCardDao = _

  /**
    * Adds a list of identity cards to the database.
    * Before the identity cards are added, the input data is first verified and validated.
    * If at least one input entry is invalid, because for example its barcode is already contained in the database,
    * the whole method terminates and no identity card is added to the database.
    * In both the successful and the unsuccessful cases, an [[AddIdentityCardsResponseDTO]] object is returned
    *
    * @param identityCardDtos The identity cards to be added
    * @return A response object, containing the result of the operation
    */
  @PUT
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Path("/add")
  @Transactional
  def addIdentityCards(identityCardDtos: Array[IdentityCardDTO]): AddIdentityCardsResponseDTO = verify(identityCardDtos) match {
    case VerifyIdentityCardsResponseDTO(true, Array(), Array()) => {
      // convert IdentityCardDTO objects to IdentityCard objects
      val identityCards = identityCardDtos.map(_.toIdentityCard)

      // insert the IdentityCard objects into the database
      identityCardDao.insert(identityCards)

      // send success result to the client
      AddIdentityCardsResponseDTO(true)
    }
    case VerifyIdentityCardsResponseDTO(false, alreadyExistingBarcodes, duplicateBarcodes) => {
      // the given identity card information are not valid
      AddIdentityCardsResponseDTO(alreadyExistingBarcodes, duplicateBarcodes)
    }
    case _ => throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
  }

  private def verify(identityCardDtos: Array[IdentityCardDTO]): VerifyIdentityCardsResponseDTO = {
    // find entries with wrong or already existing barcodes
    val alreadyExistingIdentityCardBarcodes = identityCardDtos.filter(identityCards => ValidateBarcode(identityCards.barcode) match {
      case ValidBarcode(barcode@Barcode(Prefix.IdentityCards, _, _)) => identityCardDao.exists(barcode)
      case _ => true
    }).map(_.barcode)

    // find all duplicate barcodes
    var duplicateIdentityCardBarcodes = identityCardDtos
      .map(_.barcode).groupBy(identity).collect { case (x, Array(_, _, _*)) => x }.toArray

    VerifyIdentityCardsResponseDTO(alreadyExistingIdentityCardBarcodes, duplicateIdentityCardBarcodes)
  }

  /**
    * Selects all identity cards from the database
    *
    * @return An array containing all identity cards inside the database
    */
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/all")
  @Transactional
  def selectAllIdentityCards(): Array[IdentityCardDTO] =
    identityCardDao.selectAll().map(_.toIdentityCardDTO).toArray

  /**
    * Selects all identity cards from the database, that have a barcode, which is contained in the given `barcodes` array.
    * If no identity cards can be found for a given barcode, the barcode is ignored
    *
    * @param barcodes An array containing the barcodes of all queried identity cards
    * @return An array containing the found identity cards belonging to the given barcodes
    */
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/select")
  @Transactional
  def selectIdentityCards(barcodes: Array[String]): Array[IdentityCardDTO] =
    barcodes.flatMap(barcode => ValidateBarcode(barcode) match {
      case ValidBarcode(barcode@Barcode(Prefix.IdentityCards, _, _)) => identityCardDao.selectByBarcode(barcode)
      case _ => None
    }).map(_.toIdentityCardDTO)

  /**
    * Verifies a given list of identity cards, if they:
    * - don't already exist in the database
    *
    * @param identityCards The identity card information to be checked
    * @return A response wrapping a [[VerifyIdentityCardsResponseDTO]] object
    */
  @PUT
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Path("/verify")
  @Transactional
  def verifyIdentityCards(identityCards: Array[IdentityCardDTO]): VerifyIdentityCardsResponseDTO = verify(identityCards)
}
