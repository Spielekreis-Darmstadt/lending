/**
 *
 */
package info.armado.ausleihe.client.connection

import info.armado.ausleihe.remote.dataobjects.LendGameStatusData
import info.armado.ausleihe.remote.results.AbstractResult
import info.armado.ausleihe.remote.dataobjects.information.GameInformation
import scala.util.Try

/**
 * @author marc
 *
 */
trait ServerConnectionInterface {
  def isBarcodeInUse(barcode: String): Try[AbstractResult]
  def getBarcodeStatus(barcode: String): Try[AbstractResult]
  def searchGames(searchTerm: String, title: String, author: String, publisher:String, minimumAge: Integer, playerCount: Integer, gameDuration: Integer): Try[GameInformation]
  
  def lendGames(identityCardBarcode: String, gameBarcodes: Array[String], limited: Boolean): Try[AbstractResult]
  def lendIdentityCard(identityCardBarcode: String, envelopeBarcode: String): Try[AbstractResult]
  
  def returnGame(gameBarcode: String): Try[AbstractResult]
  def returnIdentityCard(identityCardBarcode: String, envelopeBarcode: String): Try[AbstractResult]
}