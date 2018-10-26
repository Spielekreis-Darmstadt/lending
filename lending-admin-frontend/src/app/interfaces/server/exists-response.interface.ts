/**
 * An interface containing the response from the server for a query concerning the existance of an entity for a given barcode input
 *
 * @author Marc Arndt
 */
export interface ExistsResponse {
  /**
   * The barcode input, previously sent to the server
   */
  input: string,
  /**
   * The result of the query.
   * True if the barcode input exists, false otherwise
   */
  exists: boolean
}
