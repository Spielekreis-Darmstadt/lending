import {Injectable} from '@angular/core';
import {MultipleActivationModel} from '../multiple-activation.model';
import {SnotifyService} from 'ng-snotify';
import {HotRegisterer} from 'angular-handsontable';
import {isBarcodeValid} from '../../util/barcode-utility';
import {BarcodeService} from '../../core/barcode.service';

/**
 * A model classs used for the insertion of multiple games into from a table file
 */
@Injectable()
export class MultipleGameActivationModelService extends MultipleActivationModel {
  /**
   * An array containing all column definitions required for the handsontable in the confirmation step
   */
  public readonly columns: Array<any> = [
    {
      data: 'barcode',
      type: 'text',
      validator: (value, callback) => callback(isBarcodeValid(value))
    }
  ];

  /**
   * Constructor
   *
   * @param {BarcodeService} barcodeService A service used to query the server for barcode relevant information
   * @param {HotRegisterer} hotRegisterer A service used to interact with handsontable instances
   * @param {SnotifyService} snotifyService A service used to work with snotify
   */
  constructor(private barcodeService: BarcodeService, private hotRegisterer: HotRegisterer, private snotifyService: SnotifyService) {
    super();
  }

  /**
   * Inserts the given list of games in the database
   *
   * @param {Array<{barcode?: string}>} barcodeObjects The barcodes of the games to be activated
   */
  public activateItems(barcodeObjects: Array<{ barcode?: string }>): void {
    this.activationResult = null;

    const barcodes = barcodeObjects.map(barcodeObject => barcodeObject.barcode);

    this.barcodeService.activateBarcodes(barcodes, response => {
      this.activationResult = response;
    });
  }
}
