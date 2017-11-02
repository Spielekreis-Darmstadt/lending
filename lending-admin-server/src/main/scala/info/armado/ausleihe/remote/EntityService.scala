package info.armado.ausleihe.remote

import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}

import info.armado.ausleihe.database.access.{EnvelopeDao, GamesDao, IdentityCardDao}
import info.armado.ausleihe.database.barcode.{Barcode, InvalidBarcode, ValidBarcode, ValidateBarcode}
import info.armado.ausleihe.database.dataobjects.Prefix
import info.armado.ausleihe.model.{ActivationResponseDTO, ExistsResponseDTO}

@Path("/barcodes")
@RequestScoped
class EntityService {
  @Inject
  var gamesDao: GamesDao = _

  @Inject
  var identityCardDao: IdentityCardDao = _

  @Inject
  var envelopeDao: EnvelopeDao = _

  /**
    * Checks if for a given barcode input, an entity in the database exists.
    * Depending if such an entry exists an [[ExistsResponseDTO]] object is returned
    *
    * @param barcodeString The barcode string to be checked
    * @return An [[ExistsResponseDTO]] describing if an entity for the given barcode input exists inside the database
    */
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/exists/{barcode}")
  @Transactional
  def exists(@PathParam("barcode") barcodeString: String): Response = ValidateBarcode(barcodeString) match {
    case ValidBarcode(barcode @ Barcode(Prefix.Spielekreis | Prefix.BDKJ, _, _)) =>
      Response.ok(ExistsResponseDTO(barcodeString, gamesDao.exists(barcode))).build()
    case ValidBarcode(barcode @ Barcode(Prefix.IdentityCards, _, _)) =>
      Response.ok(ExistsResponseDTO(barcodeString, identityCardDao.exists(barcode))).build()
    case ValidBarcode(barcode @ Barcode(Prefix.Envelopes, _, _)) =>
      Response.ok(ExistsResponseDTO(barcodeString, envelopeDao.exists(barcode))).build()
    case ValidBarcode(_) => Response.ok(ExistsResponseDTO(barcodeString, false)).build()
    case InvalidBarcode(_) => Response.status(Response.Status.BAD_REQUEST).build()
  }

  /**
    * Activates the entities belonging to a given list of barcodes.
    * If no entity exists for a given barcode input, it is ignored.
    * Afterwards an [[ActivationResponseDTO]] instance is returned, which contains the barcodes,
    * which could be activated and which failed
    *
    * @param barcodeStrings An array of barcodes, whose entities should be activated
    * @return An [[ActivationResponseDTO]] instance containing the result of the operation
    */
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/activate")
  @Transactional
  def activate(barcodeStrings: Array[String]): Response = {
    val failedBarcodes = barcodeStrings.filter(barcodeString => ValidateBarcode(barcodeString) match {
      case ValidBarcode(barcode @ Barcode(Prefix.Spielekreis | Prefix.BDKJ, _, _)) => !gamesDao.activate(barcode)
      case ValidBarcode(barcode @ Barcode(Prefix.IdentityCards, _, _)) => !identityCardDao.activate(barcode)
      case ValidBarcode(barcode @ Barcode(Prefix.Envelopes, _, _)) => !envelopeDao.activate(barcode)
      case ValidBarcode(_) => true
      case InvalidBarcode(_) => true
    })

    Response.ok(ActivationResponseDTO(barcodeStrings.diff(failedBarcodes), failedBarcodes)).build()
  }

  /**
    * Deactivates the entities belonging to a given list of barcodes.
    * If no entity exists for a given barcode input, it is ignored.
    * Afterwards an [[ActivationResponseDTO]] instance is returned, which contains the barcodes,
    * which could be deactivated and which failed
    *
    * @param barcodeStrings An array of barcodes, whose entities should be deactivated
    * @return An [[ActivationResponseDTO]] instance containing the result of the operation
    */
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/deactivate")
  @Transactional
  def deactivate(barcodeStrings: Array[String]): Response = {
    val failedBarcodes = barcodeStrings.filter(barcodeString => ValidateBarcode(barcodeString) match {
      case ValidBarcode(barcode @ Barcode(Prefix.Spielekreis | Prefix.BDKJ, _, _)) => !gamesDao.deactivate(barcode)
      case ValidBarcode(barcode @ Barcode(Prefix.IdentityCards, _, _)) => !identityCardDao.deactivate(barcode)
      case ValidBarcode(barcode @ Barcode(Prefix.Envelopes, _, _)) => !envelopeDao.deactivate(barcode)
      case ValidBarcode(_) => true
      case InvalidBarcode(_) => true
    })

    Response.ok(ActivationResponseDTO(barcodeStrings.diff(failedBarcodes), failedBarcodes)).build()
  }
}
