import {Injectable} from '@angular/core';
import {DatabaseColumn, MultipleAdditionModel} from '../multiple-addition.model';
import {SnotifyService} from 'ng-snotify';
import {IdentityCard} from '../../interfaces/server/identity-card.interface';
import {IdentityCardService} from '../../core/identity-card.service';
import {AddIdentityCardsResponse} from '../../interfaces/server/add-identity-cards-response.interface';
import {HotTableRegisterer} from '@handsontable/angular';
import {createToBarcodeConverter} from "../../util/converters";
import {identityCardBarcodeValidator} from "../../util/validators";

/**
 * A model class used for the insertion of multiple identity cards from a table file into the database
 */
@Injectable()
export class MultipleIdentityCardAdditionModelService extends MultipleAdditionModel<IdentityCard> {
  /**
   * An array containing all database columns used for identity cards
   */
  public readonly allDatabaseHeaders: Array<DatabaseColumn<IdentityCard>> = [
    {
      title: 'Barcode',
      required: true,
      multiple: false,
      convert(value: string, entity: IdentityCard) {
        entity.barcode = value;
      }
    },
    {
      title: 'Unbenutzt',
      required: false,
      multiple: true,
      convert(value: string, entity: IdentityCard) {
        // do nothing
      }
    }];

  /**
   * An array containing all handsontable columns used for identity cards
   */
  public readonly columns: Array<any> = [
    {
      data: 'barcode',
      type: 'text',
      validator: identityCardBarcodeValidator
    }
  ];

  /**
   * An array containing all column names/labels for identity cards in the same order as in `columns`
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
        name: 'Zu Ausweis-Barcodes',
        callback: createToBarcodeConverter("33", () => this.hotRegisterer.getInstance('confirmation-hot-table'), () => this.items)
      }
    }
  };

  /**
   * Constructor
   *
   * @param identityCardService A service used to query the server for identity card relevant information
   * @param hotRegisterer A service used to interact with handsontable instances
   * @param snotifyService A service used to work with snotify
   */
  constructor(private identityCardService: IdentityCardService,
              private hotRegisterer: HotTableRegisterer,
              private snotifyService: SnotifyService) {
    super();
  }

  /**
   * Verifies the given identity cards with the server.
   * Afterwards the handsontable will be rerendered
   *
   * @param identityCards A list of identity cards to be verified
   */
  public verifyItems(identityCards: Array<IdentityCard>): void {
    this.identityCardService.verifyIdentityCards(identityCards, result => {
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
   * Inserts the given list of identity cards in the database
   *
   * @param items The identity cards to be inserted
   */
  public insertItems(items: Array<IdentityCard>): void {
    this.insertionResult = null;

    this.identityCardService.addIdentityCards(items, (response: AddIdentityCardsResponse) => {
      if (response.success) {
        this.insertionResult = {
          success: true,
          message: `${this.items.length} Ausweise wurden erfolgreich der Datenbank hinzufügt!`
        };
      } else {
        this.insertionResult = {
          success: false,
          message: 'Es ist ein Fehler beim Hinzufügen der Ausweise aufgetreten!'
        };
      }
    });
  }
}
