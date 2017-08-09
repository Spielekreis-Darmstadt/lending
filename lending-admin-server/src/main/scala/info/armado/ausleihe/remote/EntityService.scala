package info.armado.ausleihe.remote

import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.{GET, Path, PathParam, Produces}
import javax.ws.rs.core.{MediaType, Response}

import info.armado.ausleihe.database.access.{EnvelopeDao, GamesDao, IdentityCardDao}
import info.armado.ausleihe.database.barcode.{Barcode, InvalidBarcode, ValidBarcode, ValidateBarcode}
import info.armado.ausleihe.database.dataobjects.Prefix

@Path("/barcodes")
@RequestScoped
class EntityService {
  @Inject
  var gamesDao: GamesDao = _

  @Inject
  var identityCardDao: IdentityCardDao = _

  @Inject
  var envelopeDao: EnvelopeDao = _

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/exists/{barcode}")
  @Transactional
  def exists(@PathParam("barcode") barcodeString: String): Response = ValidateBarcode(barcodeString) match {
    case ValidBarcode(barcode @ Barcode(Prefix.Spielekreis | Prefix.BDKJ, _, _)) => Response.ok(gamesDao.exists(barcode)).build()
    case ValidBarcode(barcode @ Barcode(Prefix.IdentityCards, _, _)) => Response.ok(identityCardDao.exists(barcode)).build()
    case ValidBarcode(barcode @ Barcode(Prefix.Envelopes, _, _)) => Response.ok(envelopeDao.exists(barcode)).build()
    case ValidBarcode(_) => Response.ok(false).build()
    case InvalidBarcode(_) => Response.status(Response.Status.BAD_REQUEST).build()
  }
}
