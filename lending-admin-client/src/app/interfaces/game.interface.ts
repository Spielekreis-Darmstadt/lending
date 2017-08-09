/**
 * An interface containing all information required to describe a game
 *
 * @author Marc Arndt
 */
export interface Game {
  barcode: string,
  title: string,
  author?: string,
  publisher?: string,
  minAge?: string,
  playerCount?: { min: string, max: string },
  duration?: { min: string, max: string },
  comment?: string
}

/**
 * A concrete instance class for a [[Game]]
 */
export class GameInstance implements Game {
  barcode: string;
  title: string;
  author: string;
  publisher: string;
  minAge: string;
  playerCount = { min: null, max: null };
  duration = { min: null, max: null };
  comment: string;
}
