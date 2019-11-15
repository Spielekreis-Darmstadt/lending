package info.armado.ausleihe.client.model

case class SearchDetails(
    title: Option[String],
    author: Option[String],
    publisher: Option[String],
    minimumAge: Option[Integer],
    playerCount: Option[Integer],
    gameDuration: Option[Integer],
    releaseYear: Option[Integer]
)
