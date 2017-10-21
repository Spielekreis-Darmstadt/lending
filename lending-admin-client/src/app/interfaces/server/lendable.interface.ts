/**
 * An lendable item
 *
 * @author Marc Arndt
 */
export interface Lendable {
  /**
   * The barcode of the item
   */
  barcode: string
  /**
   * True if the item is activated, false otherwise
   */
  activated?: boolean
}
