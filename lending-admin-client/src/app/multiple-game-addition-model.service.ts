import {Injectable} from '@angular/core';
import {DatabaseColumn, MultipleAdditionModel} from './multiple-addition.model';
import {Game} from './interfaces/server/game.interface';
import {GameService} from './services/game.service';
import {VerificationResult} from './interfaces/verification-result.interface';
import {isBarcodeValid} from './util/barcode-utility';
import {AddGamesResponse} from './interfaces/server/add-games-response.interface';

/**
 * A model classs used for the insertion of multiple games into from a table file
 */
@Injectable()
export class MultipleGameAdditionModelService extends MultipleAdditionModel<Game> {
  /**
   * An array containing all database columns used for games
   */
  public readonly allDatabaseHeaders: Array<DatabaseColumn<Game>> = [
    {
      title: 'Barcode',
      required: true,
      multiple: false,
      convert(value: string, entity: Game) {
        entity.barcode = value;
      }
    },
    {
      title: 'Titel',
      required: true,
      multiple: false,
      convert(value: string, entity: Game) {
        entity.title = value;
      }
    },
    {
      title: 'Autor',
      required: false,
      multiple: false,
      convert(value: string, entity: Game) {
        entity.author = value;
      }
    },
    {
      title: 'Verlag',
      required: false,
      multiple: false,
      convert(value: string, entity: Game) {
        entity.publisher = value;
      }
    },
    {
      title: 'Mindestalter',
      required: false,
      multiple: false,
      convert(value: number, entity: Game) {
        entity.minAge = value;
      }
    },
    {
      title: 'Unbenutzt',
      required: false,
      multiple: true,
      convert(value: string, entity: Game) {
        // do nothing
      }
    }];

  /**
   * An array containing all handsontable columns used for games
   */
  public readonly columns: Array<any> = [
    {
      data: 'barcode',
      type: 'text',
      validator: (value, callback) => callback(isBarcodeValid(value))
    },
    {
      data: 'title',
      type: 'text',
      validator: /.+/
    },
    {
      data: 'author',
      type: 'text'
    },
    {
      data: 'publisher',
      type: 'text'
    },
    {
      data: 'minAge',
      type: 'numeric'
    },
    {
      data: 'playerCount.min',
      type: 'numeric'
    },
    {
      data: 'playerCount.max',
      type: 'numeric'
    },
    {
      data: 'duration.min',
      type: 'numeric'
    },
    {
      data: 'duration.max',
      type: 'numeric'
    },
  ];

  /**
   * An array containing all column names/labels for games in the same order as in `columns`
   */
  public readonly columnHeaders: Array<string> = [
    'Barcode',
    'Titel',
    'Autor',
    'Verlag',
    'Mindestalter',
    'Mindestspielerzahl',
    'Maximalspielerzahl',
    'Mindestdauer',
    'Maximaldauer'
  ];

  /**
   * A lambda function used to verify if a given array of games can be inserted into the database.
   * This function does both a local and a server check.
   * The server check is only done, if the local check is successful
   */
  public readonly verifyItems: (games: Array<Game>, callback: (verificationResult: VerificationResult) => void) => void =
    (games: Array<Game>, callback: (verificationResult: VerificationResult) => void) =>
      this.gameService.verifyGames(games, result => {
        if (!result) {
          callback({
            verified: false
          });
        } else {
          callback({
            verified: result.valid,
            badBarcodes: result.alreadyExistingBarcodes.concat(result.emptyTitleBarcodes)
          });
        }
      });

  /**
   * Constructor
   *
   * @param {GameService} gameService A service used to query the server for game relevant information
   */
  constructor(private gameService: GameService) {
    super();
  }

  /**
   * Inserts the given list of games in the database
   *
   * @param {Array<Game>} items The games to be inserted
   */
  public insertItems(items: Array<Game>): void {
    this.insertionResult = null;

    this.gameService.addGames(items, (response: AddGamesResponse) => {
      if (response.success) {
        this.insertionResult = {
          success: true,
          message: `${this.items.length} Items wurden erfolgreich der Datenbank hinzufügt!`
        };
      } else {
        this.insertionResult = {
          success: false,
          message: 'Es ist ein Fehler beim Hinzufügen der Items aufgetreten!'
        };
      }
    });
  }
}
