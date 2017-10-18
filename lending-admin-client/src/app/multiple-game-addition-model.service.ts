import {Injectable} from '@angular/core';
import {DatabaseColumn, MultipleAdditionModel} from './multiple-addition.model';
import {Game} from './interfaces/game.interface';
import {GameService} from './services/game.service';
import {VerificationResult} from './interfaces/verification-result.interface';
import {isBarcodeValid} from './util/barcode-utility';
import {AddGamesResponse} from './interfaces/add-games-response.interface';

@Injectable()
export class MultipleGameAdditionModelService extends MultipleAdditionModel<Game> {
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


  constructor(private gameService: GameService) {
    super();
  }

  public insertItems(items: Array<Game>): void {
    this.insertionResult = null;

    this.gameService.addGames(items, (response: AddGamesResponse) => {
      this.insertionResult = response;
    });
  }
}
