/**
 * An interface containing the response of the server for an activation or deactivation query of an array of barcodes
 *
 * @author Marc Arndt
 */
export interface ActivationResponse {
  /**
   * The successfully activated or deactivated barcodes
   */
  correctBarcodes: Array<string>,
  /**
   * The incorrect/non existent/invalid barcodes
   */
  incorrectBarcodes: Array<string>
}
