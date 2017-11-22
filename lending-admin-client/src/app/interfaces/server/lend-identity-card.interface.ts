/**
 * Information belonging to an issued identity card
 *
 * @author Marc Arndt
 */
export interface LendIdentityCard {
  /**
   * The barcode of the identity card
   */
  identityCardBarcode: string,
  /**
   * The barcode of the envelope, bound to the identity card
   */
  envelopeBarcode: string,
  /**
   * The time + date, when the identity card has been issued
   */
  lendTime: Date,
  /**
   * The number of currently lend games by the identity card
   */
  numberOfLendGames: number
}
