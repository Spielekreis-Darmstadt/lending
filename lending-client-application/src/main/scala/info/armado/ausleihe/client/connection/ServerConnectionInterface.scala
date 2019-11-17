/**
  *
  */
package info.armado.ausleihe.client.connection

import info.armado.ausleihe.client.model.SearchDetails
import info.armado.ausleihe.client.transport.dataobjects.information.GameInformationDTO
import info.armado.ausleihe.client.transport.results.AbstractResultDTO

import scala.util.Try

/**
  * @author marc
  *
  */
trait ServerConnectionInterface {
  def isBarcodeInUse(barcode: String): Try[AbstractResultDTO]

  def getBarcodeStatus(barcode: String): Try[AbstractResultDTO]

  def searchGames(searchTerm: String, searchDetails: SearchDetails): Try[GameInformationDTO]

  def lendGames(
      identityCardBarcode: String,
      gameBarcodes: Array[String],
      limited: Boolean
  ): Try[AbstractResultDTO]

  def lendIdentityCard(identityCardBarcode: String, envelopeBarcode: String): Try[AbstractResultDTO]

  def returnGame(gameBarcode: String): Try[AbstractResultDTO]

  def returnIdentityCard(
      identityCardBarcode: String,
      envelopeBarcode: String
  ): Try[AbstractResultDTO]
}
