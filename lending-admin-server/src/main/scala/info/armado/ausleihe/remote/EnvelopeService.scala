package info.armado.ausleihe.remote

import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}

import info.armado.ausleihe.database.access.EnvelopeDao
import info.armado.ausleihe.database.barcode.{Barcode, ValidBarcode, ValidateBarcode}
import info.armado.ausleihe.database.dataobjects.Prefix
import info.armado.ausleihe.database.entities.Envelope
import info.armado.ausleihe.model.transport.{AddEnvelopesResponseDTO, EnvelopeDTO, VerifyEnvelopesResponseDTO}

@Path("/envelopes")
@RequestScoped
class EnvelopeService {
  @Inject
  var envelopeDao: EnvelopeDao = _

  /**
    * Adds a list of envelopes to the database.
    * Before the envelopes are added, the input data is first verified and validated.
    * If at least one input entry is invalid, because for example its barcode is already contained in the database,
    * the whole method terminates and no envelope is added to the database.
    * In both the successful and the unsuccessful cases, an [[AddEnvelopesResponseDTO]] object is returned
    *
    * @param envelopeDtos The envelopes to be added
    * @return A response object, containing the result of the operation
    */
  @PUT
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Path("/add")
  @Transactional
  def addIdentityCards(envelopeDtos: Array[EnvelopeDTO]): Response = verify(envelopeDtos) match {
    case VerifyEnvelopesResponseDTO(true, Array(), Array()) => {
      // convert EnvelopeDTO objects to Envelope objects
      val envelopes = envelopeDtos.map(envelopeDto => toEnvelope(envelopeDto))

      // insert the Envelope objects into the database
      envelopeDao.insert(envelopes)

      // send success result to the client
      Response.ok(AddEnvelopesResponseDTO(true)).build()
    }
    case VerifyEnvelopesResponseDTO(false, alreadyExistingBarcodes, duplicateBarcodes) => {
      // the given envelope information are not valid
      Response.ok(AddEnvelopesResponseDTO(alreadyExistingBarcodes, duplicateBarcodes)).build()
    }
    case _ => Response.status(Response.Status.PRECONDITION_FAILED).build()
  }

  /**
    * Selects all envelopes from the database
    *
    * @return An array containing all envelopes inside the database
    */
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/all")
  @Transactional
  def selectAllEnvelopes(): Response = Response.ok(
    envelopeDao.selectAll().map(envelope => toEnvelopeDTO(envelope)).toArray).build()

  /**
    * Selects all envelopes from the database, that have a barcode, which is contained in the given `barcodes` array.
    * If no envelopes can be found for a given barcode, the barcode is ignored
    *
    * @param barcodes An array containing the barcodes of all queried envelopes
    * @return An array containing the found envelopes belonging to the given barcodes
    */
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/select")
  @Transactional
  def selectEnvelopes(barcodes: Array[String]): Response = Response.ok(
    barcodes.flatMap(barcode => ValidateBarcode(barcode) match {
      case ValidBarcode(barcode@Barcode(Prefix.Envelopes, _, _)) => envelopeDao.selectByBarcode(barcode)
      case _ => None
    }).map(identityCard => toEnvelopeDTO(identityCard))
  ).build()

  /**
    * Verifies a given list of envelopes, if they:
    * - don't already exist in the database
    *
    * @param envelopes The envelope information to be checked
    * @return A response wrapping a [[VerifyEnvelopesResponseDTO]] object
    */
  @PUT
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Path("/verify")
  @Transactional
  def verifyEnvelopes(envelopes: Array[EnvelopeDTO]): Response = Response.ok(verify(envelopes)).build()

  private def verify(envelopeDtos: Array[EnvelopeDTO]): VerifyEnvelopesResponseDTO = {
    // find entries with wrong or already existing barcodes
    val alreadyExistingIdentityCardBarcodes = envelopeDtos.filter(identityCards => ValidateBarcode(identityCards.barcode) match {
      case ValidBarcode(barcode@Barcode(Prefix.Envelopes, _, _)) => envelopeDao.exists(barcode)
      case _ => true
    }).map(_.barcode)

    // find all duplicate barcodes
    var duplicateIdentityCardBarcodes = envelopeDtos
      .map(_.barcode).groupBy(identity).collect { case (x, Array(_, _, _*)) => x }.toArray

    VerifyEnvelopesResponseDTO(alreadyExistingIdentityCardBarcodes, duplicateIdentityCardBarcodes)
  }

  private def toEnvelopeDTO(envelope: Envelope): EnvelopeDTO = {
    val result = new EnvelopeDTO()

    result.barcode = envelope.barcode.toString

    result.activated = envelope.available

    result
  }

  private def toEnvelope(envelopeDto: EnvelopeDTO): Envelope = {
    val result = Envelope(Barcode(envelopeDto.barcode))

    Option(envelopeDto.activated).foreach(activated => result.available = activated)

    result
  }
}
