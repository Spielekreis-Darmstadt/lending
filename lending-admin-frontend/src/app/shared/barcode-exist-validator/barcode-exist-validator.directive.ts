import {Directive, Input} from '@angular/core';
import {AbstractControl, ValidationErrors, AsyncValidator, NG_ASYNC_VALIDATORS} from "@angular/forms";
import {isBarcodeValid} from "../../util/barcode-utility";
import {EntityService} from "../../core/entity.service";
import { Observable } from 'rxjs';
import {isArray, isString} from "util";

/**
 * A validator used to check if a given barcode is valid.
 * A barcode is valid if it:
 * - starts with a valid prefix
 * - contains a valid/correct checksum
 * - exists yet
 *
 * @author Marc Arndt
 */
@Directive({
  selector: '[lendingBarcodeExistValidator]',
  providers: [{provide: NG_ASYNC_VALIDATORS, useExisting: BarcodeExistValidatorDirective, multi: true}]
})
export class BarcodeExistValidatorDirective implements AsyncValidator {

  /**
   * An optional accepted barcode prefix, or a list of accepted barcode prefixes
   * @type {string}
   */
  @Input('lendingBarcodeExistValidator')
  public acceptedPrefix: string | Array<string> = '';

  /**
   * Constructor
   * @param {EntityService} entityService The barcode service used to check if a barcode exists
   */
  constructor(private entityService: EntityService) {
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
    return this.entityService.validateBarcodeNotExists(value);
  }
}
