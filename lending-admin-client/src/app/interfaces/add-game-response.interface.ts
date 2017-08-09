/**
 * An interface containing all information used in an `AddGameResponse` from the server,
 * which is sent when the client wants to add a new game to the server
 *
 * @author Marc Arndt
 */
export interface AddGameResponse {
  success: boolean,
  responseMessage: string
}
