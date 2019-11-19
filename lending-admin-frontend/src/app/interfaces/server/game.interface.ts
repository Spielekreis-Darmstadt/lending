import { Lendable } from './lendable.interface';

/**
 * An interface containing all information required to describe a game
 *
 * @author Marc Arndt
 */
export interface Game extends Lendable {
  barcode: string,
  title: string,
  author?: string,
  publisher?: string,
  minAge?: number,
  playerCount?: { min?: number | string, max?: number | string },
  duration?: { min?: number | string, max?: number | string },
  releaseYear?: number,
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
  releaseYear: number;
  comment: string;
  activated: boolean;
}
