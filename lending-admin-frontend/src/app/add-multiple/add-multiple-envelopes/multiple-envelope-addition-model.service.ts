import {Injectable} from '@angular/core';
import {DatabaseColumn, MultipleAdditionModel} from '../multiple-addition.model';
import {createBarcode, isBarcodeValid} from '../../util/barcode-utility';
import {SnotifyService} from 'ng-snotify';
import {Envelope} from '../../interfaces/server/envelope.interface';
import {EnvelopeService} from '../../core/envelope.service';
import {AddEnvelopesResponse} from '../../interfaces/server/add-envelopes-response.interface';
import {isString} from 'util';
import {HotTableRegisterer} from '@handsontable/angular';

/**
 * A model class used for the insertion of multiple envelopes from a table file into the database
 */
@Injectable()
export class MultipleEnvelopeAdditionModelService extends MultipleAdditionModel<Envelope> {
  /**
   * An array containing all database columns used for envelopes
   */
  public readonly allDatabaseHeaders: Array<DatabaseColumn<Envelope>> = [
    {
      title: 'Barcode',
      required: true,
      multiple: false,
      convert(value: string, entity: Envelope) {
        entity.barcode = value;
      }
    },
    {
      title: 'Unbenutzt',
      required: false,
      multiple: true,
      convert(value: string, entity: Envelope) {
        // do nothing
      }
    }];

  /**
   * An array containing all handsontable columns used for envelopes
   */
  public readonly columns: Array<any> = [
    {
      data: 'barcode',
      type: 'text',
      validator: (value, callback) => callback(isString(value) && value.startsWith('44') && isBarcodeValid(value))
    }
  ];

  /**
   * An array containing all column names/labels for envelopes in the same order as in `columns`
   */
  public readonly columnHeaders: Array<string> = [
    'Barcode'
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
        name: 'Zu Umschlag-Barcodes',
        callback: (key, options) => {
          const hot = this.hotRegisterer.getInstance('confirmation-hot-table');

          const fromRow = hot.getSelectedLast()[0];
          const toRow = hot.getSelectedLast()[2] + 1;

          this.items.slice(fromRow, toRow)
            .forEach(game => {
              // only convert barcode strings that are made up of only the index part
              if (game.barcode.length <= 5) {
                game.barcode = createBarcode('44', game.barcode);
              }
            });

          hot.render();
        }
      }
    }
  };

  /**
   * Constructor
   *
   * @param envelopeService A service used to query the server for envelope relevant information
   * @param hotRegisterer A service used to interact with handsontable instances
   * @param snotifyService A service used to work with snotify
   */
  constructor(private envelopeService: EnvelopeService, private hotRegisterer: HotTableRegisterer, private snotifyService: SnotifyService) {
    super();
  }

  /**
   * Verifies the given envelopes with the server.
   * Afterwards the handsontable will be rerendered
   *
   * @param  envelopes A list of envelopes to be verified
   */
  public verifyItems(envelopes: Array<Envelope>): void {
    this.envelopeService.verifyEnvelopes(envelopes, result => {
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

        if (result.valid) {
          this.snotifyService.success('Alle Einträge sind valide', {timeout: 0});
        }
      }

      hot.render();
    });
  }

  /**
   * Inserts the given list of envelopes in the database
   *
   * @param items The envelopes to be inserted
   */
  public insertItems(items: Array<Envelope>): void {
    this.insertionResult = null;

    this.envelopeService.addEnvelopes(items, (response: AddEnvelopesResponse) => {
      if (response.success) {
        this.insertionResult = {
          success: true,
          message: `${this.items.length} Umschläge wurden erfolgreich der Datenbank hinzufügt!`
        };
      } else {
        this.insertionResult = {
          success: false,
          message: 'Es ist ein Fehler beim Hinzufügen der Umschläge aufgetreten!'
        };
      }
    });
  }
}
