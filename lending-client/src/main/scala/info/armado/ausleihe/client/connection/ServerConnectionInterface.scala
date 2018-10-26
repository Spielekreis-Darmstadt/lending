/**
  *
  */
package info.armado.ausleihe.client.connection

import info.armado.ausleihe.remote.client.dataobjects.information.GameInformationDTO
import info.armado.ausleihe.remote.client.results.AbstractResultDTO

import scala.util.Try

/**
  * @author marc
  *
  */
trait ServerConnectionInterface {
  def isBarcodeInUse(barcode: String): Try[AbstractResultDTO]

  def getBarcodeStatus(barcode: String): Try[AbstractResultDTO]

  def searchGames(searchTerm: String, title: String, author: String, publisher: String, minimumAge: Integer, playerCount: Integer, gameDuration: Integer): Try[GameInformationDTO]

  def lendGames(identityCardBarcode: String, gameBarcodes: Array[String], limited: Boolean): Try[AbstractResultDTO]

  def lendIdentityCard(identityCardBarcode: String, envelopeBarcode: String): Try[AbstractResultDTO]

  def returnGame(gameBarcode: String): Try[AbstractResultDTO]

  def returnIdentityCard(identityCardBarcode: String, envelopeBarcode: String): Try[AbstractResultDTO]
}