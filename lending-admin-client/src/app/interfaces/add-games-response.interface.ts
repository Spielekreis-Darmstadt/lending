/**
 * An interface containing all information used in an `AddGamesResponse` from the server,
 * which is sent when the client wants to add a new game to the server
 *
 * @author Marc Arndt
 */
export interface AddGamesResponse {
  success: boolean,
  alreadyExistingBarcodes: Array<string>,
  emptyTitleBarcodes: Array<string>
}
