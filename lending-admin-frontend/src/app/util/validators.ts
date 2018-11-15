import {isString} from "util";
import {isBarcodeValid} from "./barcode-utility";

export function gameBarcodeValidator(input: any, callback: (valid: boolean) => void): void {
  // ensure, that the given input is a string
  const value = input.toString();

  callback(isString(value) && (value.startsWith('11') || value.startsWith('22')) && isBarcodeValid(value))
}

export function identityCardBarcodeValidator(input: any, callback: (valid: boolean) => void): void {
  // ensure, that the given input is a string
  const value = input.toString();

  callback(isString(value) && value.startsWith('33') && isBarcodeValid(value))
}

export function envelopeBarcodeValidator(input: any, callback: (valid: boolean) => void): void {
  // ensure, that the given input is a string
  const value = input.toString();

  callback(isString(value) && value.startsWith('44') && isBarcodeValid(value))
}
