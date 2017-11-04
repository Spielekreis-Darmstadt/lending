/**
 * An response send by the server, when a list of inputted identity card information should be verified
 *
 * @author Marc Arndt
 */
export interface VerifyIdentityCardsResponse {
  /**
   * True if all sent identity card information are valid and can be added, false otherwise
   */
  valid: boolean,
  /**
   * An array containing all barcodes, that were sent to the server, which already exist
   */
  alreadyExistingBarcodes?: Array<string>
}
