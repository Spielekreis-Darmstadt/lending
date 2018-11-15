import {Injectable} from '@angular/core';
import {MultipleActivationModel} from '../multiple-activation.model';
import {EntityService} from '../../core/entity.service';
import {Lendable} from "../../interfaces/server/lendable.interface";
import {gameBarcodeValidator} from "../../util/validators";

/**
 * A model class used for the activation of multiple games from a table file
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
      validator: gameBarcodeValidator
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
   * @param lendables The barcodes of the games to be activated
   */
  public activateItems(lendables: Array<Lendable>): void {
    this.activationResult = null;

    const barcodes = lendables.map(lendable => lendable.barcode);

    this.entityService.activateBarcodes(barcodes, response => {
      this.activationResult = response;
    });
  }
}
