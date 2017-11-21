/**
 * Information belonging to a borrowed game
 *
 * @author Marc Arndt
 */
export interface LendGame {
  /**
   * The barcode of the borrowed game
   */
  barcode: string,
  /**
   * The time + date, when the game has been borrowed
   */
  lendTime: Date
}

/**
 * A group of borrowed game, that have been borrowed by the same identity card
 *
 *
 * @author Marc Arndt
 */
export interface LendIdentityCardGroup {
  /**
   * The barcode of the identity card
   */
  barcode: string,
  /**
   * The games, which have been borrowed by the identity card
   */
  lendGames: Array<LendGame>
}
