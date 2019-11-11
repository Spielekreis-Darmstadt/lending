package info.armado.ausleihe.client.model

case class SearchDetails(
    title: String,
    author: String,
    publisher: String,
    minimumAge: Integer,
    playerCount: Integer,
    gameDuration: Integer,
    releaseYear: Integer
)
