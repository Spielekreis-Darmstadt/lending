import {Injectable} from '@angular/core';
import {DatabaseColumn, MultipleAdditionModel} from '../multiple-addition.model';
import {Game} from '../../interfaces/server/game.interface';
import {GameService} from '../../core/game.service';
import {AddGamesResponse} from '../../interfaces/server/add-games-response.interface';
import {SnotifyService} from 'ng-snotify';
import {isString} from 'util';
import {HotTableRegisterer} from '@handsontable/angular';
import {gameBarcodeValidator} from "../../util/validators";
import {createToBarcodeConverter} from "../../util/converters";

/**
 * A model class used for the insertion of multiple games from a table file into the database
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
      title: 'Spieldauer (Min - Max)',
      required: false,
      multiple: false,
      convert(value: string, entity: Game) {
        const regex = /^(\d+) ?(-|–) ?(\d+)$/;

        if (isString(value) && value.match(regex)) {
          const match = regex.exec(value);

          entity.duration = {
            min: parseInt(match[1], 10),
            max: parseInt(match[3], 10)
          };
        } else {
          entity.duration = {
            min: value,
            max: value
          };
        }
      }
    },
    {
      title: 'Mindestspieldauer',
      required: false,
      multiple: false,
      convert(value: number, entity: Game) {
        if (entity.duration) {
          entity.duration.min = value;
        } else {
          entity.duration = {min: value, max: 'Keine Angabe'};
        }
      }
    },
    {
      title: 'Maximalspieldauer',
      required: false,
      multiple: false,
      convert(value: number, entity: Game) {
        if (entity.duration) {
          entity.duration.max = value;
        } else {
          entity.duration = {min: 'Keine Angabe', max: value};
        }
      }
    },
    {
      title: 'Spielerzahl (Min - Max)',
      required: false,
      multiple: false,
      convert(value: string, entity: Game) {
        const regex = /^(\d+) ?(-|–) ?(\d+)$/;

        if (isString(value) && value.match(regex)) {
          const match = regex.exec(value);

          entity.playerCount = {
            min: parseInt(match[1], 10),
            max: parseInt(match[3], 10)
          };
        } else {
          entity.playerCount = {
            min: value,
            max: value
          };
        }
      }
    },
    {
      title: 'Mindestspielerzahl',
      required: false,
      multiple: false,
      convert(value: number, entity: Game) {
        if (entity.playerCount) {
          entity.playerCount.min = value;
        } else {
          entity.playerCount = {min: value, max: 'Keine Angabe'};
        }
      }
    },
    {
      title: 'Maximalspielerzahl',
      required: false,
      multiple: false,
      convert(value: number, entity: Game) {
        if (entity.playerCount) {
          entity.playerCount.max = value;
        } else {
          entity.playerCount = {min: 'Keine Angabe', max: value};
        }
      }
    },
    {
      title: 'Erscheinungsjahr',
      required: false,
      multiple: false,
      convert(value: number, entity: Game) {
        entity.releaseYear = value; 
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
      validator: gameBarcodeValidator
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
    {
      data: 'releaseYear',
      type: 'numeric'
    }
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
    'Maximaldauer',
    'Erscheinungsjahr'
  ];

  /**
   * The content menu settings for the handsontable in the confirmation step
   */
  public readonly contextMenuItems: object = {
    items: {
      'row_above': {},
      'row_below': {},
      'hsep1': '---------',
      'remove_row': {},
      'hsep2': '---------',
      'converter1': {
        name: 'Zu Spielekreis-Barcodes',
        callback: createToBarcodeConverter("11", () => this.hotRegisterer.getInstance('confirmation-hot-table'), () => this.items)
      },
      'converter2': {
        name: 'Zu BDKJ-Barcodes',
        callback: createToBarcodeConverter("22", () => this.hotRegisterer.getInstance('confirmation-hot-table'), () => this.items)
      }
    }
  };

  /**
   * Constructor
   *
   * @param gameService A service used to query the server for game relevant information
   * @param hotRegisterer A service used to interact with handsontable instances
   * @param snotifyService A service used to work with snotify
   */
  constructor(private gameService: GameService, private hotRegisterer: HotTableRegisterer, private snotifyService: SnotifyService) {
    super();
  }

  /**
   * Verifies the given games with the server.
   * Afterwards the handsontable will be rerendered
   *
   * @param games A list of games to be verified
   */
  public verifyItems(games: Array<Game>): void {
    this.gameService.verifyGames(games, result => {
      const hot = this.hotRegisterer.getInstance('confirmation-hot-table');

      if (!result) {
        this.verificationResult = {
          verified: false
        };

        this.snotifyService.error('Es ist ein unerwarteter Fehler aufgetreten', {timeout: 0});
      } else {
        this.verificationResult = {
          verified: result.valid,
          badBarcodes: [],
          duplicateBarcodes: []
        };

        if (result.alreadyExistingBarcodes && result.alreadyExistingBarcodes.length > 0) {
          this.snotifyService.warning(
            `Bei ${result.alreadyExistingBarcodes.length} Einträgen existiert der Barcode bereits`,
            {timeout: 0}
          );
          this.verificationResult.badBarcodes.push(...result.alreadyExistingBarcodes);
        }
        if (result.duplicateBarcodes && result.duplicateBarcodes.length > 0) {
          this.snotifyService.warning(
            `${result.duplicateBarcodes.length} Einträgen haben einen mehrfach existierenden Barcode`,
            {timeout: 0}
          );
          this.verificationResult.duplicateBarcodes.push(...result.duplicateBarcodes);
        }
        if (result.emptyTitleBarcodes && result.emptyTitleBarcodes.length > 0) {
          this.snotifyService.warning(
            `Bei ${result.emptyTitleBarcodes.length} Einträgen fehlt der Titel`,
            {timeout: 0}
          );
          this.verificationResult.badBarcodes.push(...result.emptyTitleBarcodes);
        }

        if (result.valid) {
          this.snotifyService.success('Alle Einträge sind valide', {timeout: 0});
        }
      }

      hot.render();
    });
  }

  /**
   * Inserts the given list of games in the database
   *
   * @param items The games to be inserted
   */
  public insertItems(items: Array<Game>): void {
    this.insertionResult = null;

    this.gameService.addGames(items, (response: AddGamesResponse) => {
      if (response.success) {
        this.insertionResult = {
          success: true,
          message: `${this.items.length} Spiele wurden erfolgreich der Datenbank hinzufügt!`
        };
      } else {
        this.insertionResult = {
          success: false,
          message: 'Es ist ein Fehler beim Hinzufügen der Spiele aufgetreten!'
        };
      }
    });
  }
}
