package info.armado.ausleihe.admin.model

import java.util.concurrent.ConcurrentHashMap
import javax.enterprise.context.ApplicationScoped
import javax.websocket.Session

@ApplicationScoped
class Clients {
  var lastUpdate: Option[OverviewUpdate] = None

  val sessions = new ConcurrentHashMap[Session, Unit]

  def sendUpdate(update: OverviewUpdate): Unit = {
    this.lastUpdate = Option(update)

    this.sessions.forEach((session, _) => sendToSession(session, update))
  }

  def addUser(session: Session): Unit = {
    this.lastUpdate.foreach(sendToSession(session, _))

    this.sessions.put(session, Unit)
  }

  private def sendToSession(session: Session, update: OverviewUpdate): Unit =
    session.getBasicRemote.sendText(update.toJSON)
}

class OverviewUpdate(numberOfGames: Long, numberOfLendGames: Long,
                     numberOfIdentityCards: Long, numberOfLendIdentityCards: Long) {
  def toJSON: String =
    s"""
       |{
       |  "numberOfGames": ${numberOfGames},
       |  "numberOfLendGames": ${numberOfLendGames},
       |  "numberOfIdentityCards": ${numberOfIdentityCards},
       |  "numberOfLendIdentityCards": ${numberOfLendIdentityCards}
       |}
    """.stripMargin
}