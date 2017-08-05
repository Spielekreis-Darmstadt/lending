package info.armado.ausleihe.remote

import javax.enterprise.context.RequestScoped
import javax.transaction.Transactional
import javax.ws.rs.core.MediaType
import javax.ws.rs.{GET, Path, Produces}

import info.armado.ausleihe.model.TestInstance

@Path("/test")
@RequestScoped
class TestService {
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Path("/check")
  @Transactional
  def test(): TestInstance = {
    val result = new TestInstance()

    result.name = "Bob!"
    result.attribute = 7

    result
  }
}
