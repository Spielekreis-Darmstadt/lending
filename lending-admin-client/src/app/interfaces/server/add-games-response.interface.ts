/**
 * An interface containing all information used in an `AddGamesResponse` from the server,
 * which is sent when the client wants to add a new game to the server
 *
 * @author Marc Arndt
 */
export interface AddGamesResponse {
  /**
   * True if the games were successfully added, false otherwise
   */
  success: boolean,
  /**
   * An array containing all barcodes, that were sent to the server, which already exist
   */
  alreadyExistingBarcodes?: Array<string>,
  /**
   * An array containing barcodes belonging to entries that contain an empty title
   */
  emptyTitleBarcodes?: Array<string>
}
