import {Directive, Input} from '@angular/core';
import {AbstractControl, ValidationErrors, AsyncValidator, NG_ASYNC_VALIDATORS} from "@angular/forms";
import {isBarcodeValid} from "../../util/barcode-utility";
import {BarcodeService} from "../../core/barcode.service";
import {Observable} from "rxjs/Observable";
import 'rxjs/add/operator/map'
import {isArray, isString} from "util";

/**
 * A validator used to check if a given barcode is valid.
 * A barcode is valid if it:
 * - starts with a valid prefix
 * - contains a valid/correct checksum
 * - doesn't exist yet
 *
 * @author Marc Arndt
 */
@Directive({
  selector: '[lendingBarcodeValidator]',
  providers: [{provide: NG_ASYNC_VALIDATORS, useExisting: BarcodeValidatorDirective, multi: true}]
})
export class BarcodeValidatorDirective implements AsyncValidator {

  /**
   * An optional accepted barcode prefix, or a list of accepted barcode prefixes
   * @type {string}
   */
  @Input('lendingBarcodeValidator')
  public acceptedPrefix: string | Array<string> = '';

  /**
   * Constructor
   * @param {BarcodeService} barcodeService The barcode service used to check if a barcode exists
   */
  constructor(private barcodeService: BarcodeService) {
  }

  validate(control: AbstractControl): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> {
    const value = control.value.toString();

    /*
     * Check if the barcode starts with an accepted prefix
     */
    if (!(isString(this.acceptedPrefix) && value.startsWith(this.acceptedPrefix)) &&
      !(isArray(this.acceptedPrefix) && (<Array<string>>this.acceptedPrefix).some(prefix => value.startsWith(prefix)))) {
      return new Promise((fulfill, reject) => fulfill({'invalidPrefix': {value: control.value}}));
    }

    /*
     * Check if the barcode is valid (i.e. it contains a valid/correct checksum)
     */
    if (!isBarcodeValid(value)) {
      return new Promise((fulfill, reject) => fulfill({'invalidBarcode': {'value': control.value}}));
    }

    /*
     * Check if the barcode already exists
     */
    return this.barcodeService.validateBarcodeExists(value);
  }
}
