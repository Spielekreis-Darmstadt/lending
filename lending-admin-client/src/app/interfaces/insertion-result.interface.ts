/**
 * A result for an insertion operation for a list of items on the server
 *
 * @author Marc Arndt
 */
export interface InsertionResult {
  /**
   * True if the items were successfully inserted/added, false otherwise
   */
  success: boolean,
  /**
   * A message, containing a message that describes either the success, or the failure reason
   */
  message?: string
}
