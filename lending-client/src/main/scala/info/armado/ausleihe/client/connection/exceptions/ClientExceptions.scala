package info.armado.ausleihe.client.connection.exceptions

import javax.ws.rs.ProcessingException
import java.net.URI
import java.net.ConnectException
import info.armado.ausleihe.client.model.ConnectionProperties
import info.armado.ausleihe.client.model.Configuration

case class NotFoundException(val serverAddress: URI) extends Exception
case class BadRequestException(val serverAddress: URI) extends Exception
case class InternalServerErrorException(val serverAddress: URI) extends Exception
case class UnhandledStatusCodeException(val serverAddress: URI, val statusCode: Int) extends Exception

object ServerUnreachableException {
  def unapply(connectException: ConnectException): Option[URI] = Some(new URI(Configuration.connectionProperties.baseURL))
}

object ProcessingExceptionWrapper {
  def unapply(processException: ProcessingException): Option[Throwable] = Option(processException.getCause)
}