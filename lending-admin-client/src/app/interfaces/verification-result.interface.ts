/**
 * A result for a verification operation for a list of items on the server
 *
 * @author Marc Arndt
 */
export interface VerificationResult {
  /**
   * True if the items are all valid, false otherwise
   */
  verified: boolean,
  /**
   * An array containing all sent barcodes, that are for some reason bad.
   * The reason depends on the kind of items
   */
  badBarcodes?: Array<string>
}
