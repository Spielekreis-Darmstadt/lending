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
  minAge?: number,
  playerCount?: { min: number, max: number },
  duration?: { min: number, max: number },
  comment?: string,
  activated?: boolean
}

/**
 * A concrete instance class for a [[Game]]
 */
export class GameInstance implements Game {
  barcode: string;
  title: string;
  author: string;
  publisher: string;
  minAge: number;
  playerCount = { min: null, max: null };
  duration = { min: null, max: null };
  comment: string;
  activated: boolean;
}
