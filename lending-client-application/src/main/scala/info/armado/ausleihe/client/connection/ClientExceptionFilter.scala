package info.armado.ausleihe.client.connection

import javax.ws.rs.client.ClientResponseFilter
import javax.ws.rs.client.ClientRequestContext
import javax.ws.rs.client.ClientResponseContext
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status._
import info.armado.ausleihe.client.connection.exceptions._

class ClientExceptionFilter extends ClientResponseFilter {  
  def filter(reqCtx: ClientRequestContext, resCtx: ClientResponseContext): Unit = Response.Status.fromStatusCode(resCtx.getStatus) match {
    case BAD_REQUEST => throw BadRequestException(reqCtx.getUri)
    case NOT_FOUND => throw NotFoundException(reqCtx.getUri)
    case INTERNAL_SERVER_ERROR => throw InternalServerErrorException(reqCtx.getUri)
    case OK => // Do nothing
    case status => throw UnhandledStatusCodeException(reqCtx.getUri, status.getStatusCode) 
  }
}