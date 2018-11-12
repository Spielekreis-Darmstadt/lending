package info.armado.ausleihe.admin.model

case class OverviewUpdate(numberOfGames: Long, numberOfLendGames: Long, numberOfIdentityCards: Long, numberOfLendIdentityCards: Long) {
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
