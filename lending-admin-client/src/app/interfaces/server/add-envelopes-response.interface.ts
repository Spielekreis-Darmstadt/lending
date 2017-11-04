/**
 * An interface containing all information used in an `AddEnvelopesResponse` from the server,
 * which is sent when the client wants to add a new envelope to the server
 *
 * @author Marc Arndt
 */
export interface AddEnvelopesResponse {
  /**
   * True if the envelopes were successfully added, false otherwise
   */
  success: boolean,
  /**
   * An array containing all barcodes, that were sent to the server, which already exist
   */
  alreadyExistingBarcodes?: Array<string>
}
