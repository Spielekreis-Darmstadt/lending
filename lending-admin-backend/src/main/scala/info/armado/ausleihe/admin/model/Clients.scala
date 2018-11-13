package info.armado.ausleihe.admin.model

import java.util.concurrent.ConcurrentHashMap

import javax.enterprise.context.ApplicationScoped
import javax.websocket.Session

@ApplicationScoped
class Clients(private var lastUpdate: Option[OverviewUpdate], val sessions: ConcurrentHashMap[Session, Unit]) {
  def this() = this(None, new ConcurrentHashMap())

  def isEmpty: Boolean = sessions.isEmpty

  def sendUpdate(update: OverviewUpdate): Unit = {
    this.lastUpdate = Option(update)

    this.sessions.keySet().forEach(session => sendToSession(session, update))
  }

  def addUser(session: Session): Unit = {
    this.lastUpdate.foreach(sendToSession(session, _))

    this.sessions.put(session, Unit)
  }

  private def sendToSession(session: Session, update: OverviewUpdate): Unit =
    session.getBasicRemote.sendText(update.toJSON)
}