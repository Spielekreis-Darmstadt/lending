/**
 * An response send by the server, when a list of inputted game information should be verified
 *
 * @author Marc Arndt
 */
export interface VerifyGamesResponse {
  /**
   * True if all sent game information are valid and can be added, false otherwise
   */
  valid: boolean,
  /**
   * An array containing all barcodes, that were sent to the server, which already exist
   */
  alreadyExistingBarcodes?: Array<string>,
  /**
   * An array containing all duplicate barcodes, contained in the data
   */
  duplicateBarcodes?: Array<string>,
  /**
   * An array containing barcodes belonging to entries that contain an empty title
   */
  emptyTitleBarcodes?: Array<string>
}
