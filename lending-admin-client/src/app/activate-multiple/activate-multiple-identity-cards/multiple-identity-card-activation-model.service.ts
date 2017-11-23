import {Injectable} from '@angular/core';
import {MultipleActivationModel} from '../multiple-activation.model';
import {isBarcodeValid} from '../../util/barcode-utility';
import {EntityService} from '../../core/entity.service';
import {isString} from 'util';

/**
 * A model class used for the activation of multiple identity cards from a table file
 */
@Injectable()
export class MultipleIdentityCardActivationModelService extends MultipleActivationModel {
  /**
   * An array containing all column definitions required for the handsontable in the confirmation step
   */
  public readonly columns: Array<any> = [
    {
      data: 'barcode',
      type: 'text',
      validator: (value, callback) => callback(isString(value) && value.startsWith('33') && isBarcodeValid(value))
    }
  ];

  /**
   * Constructor
   *
   * @param {EntityService} entityService A service used to query the server for barcode relevant information
   */
  constructor(private entityService: EntityService) {
    super();
  }

  /**
   * Activates the games belonging to the given list of barcodes inside the database
   *
   * @param {Array<{barcode?: string}>} barcodeObjects The barcodes of the identity cards to be activated
   */
  public activateItems(barcodeObjects: Array<{ barcode?: string }>): void {
    this.activationResult = null;

    const barcodes = barcodeObjects.map(barcodeObject => barcodeObject.barcode);

    this.entityService.activateBarcodes(barcodes, response => {
      this.activationResult = response;
    });
  }
}
