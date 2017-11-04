/**
 * An interface containing all information used in an `AddIdentityCardsResponse` from the server,
 * which is sent when the client wants to add a new identity cards to the server
 *
 * @author Marc Arndt
 */
export interface AddIdentityCardsResponse {
  /**
   * True if the identity cards were successfully added, false otherwise
   */
  success: boolean,
  /**
   * An array containing all barcodes, that were sent to the server, which already exist
   */
  alreadyExistingBarcodes?: Array<string>
}
