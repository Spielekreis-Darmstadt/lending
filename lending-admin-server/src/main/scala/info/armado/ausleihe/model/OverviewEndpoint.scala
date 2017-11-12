package info.armado.ausleihe.model

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.websocket._
import javax.websocket.server.ServerEndpoint

@ApplicationScoped
@ServerEndpoint("/overview")
class OverviewEndpoint {
  @Inject
  var clients: Clients = _

  @OnOpen
  def onOpen(session: Session, conf: EndpointConfig): Unit = this.clients.addUser(session)

  @OnClose
  def onClone(session: Session, reason: CloseReason): Unit = this.clients.sessions.remove(session)

  @OnError
  def onError(session: Session, error: Throwable): Unit = this.clients.sessions.remove(session)
}
