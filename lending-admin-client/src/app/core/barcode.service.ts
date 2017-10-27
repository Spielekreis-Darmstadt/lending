import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable} from 'rxjs/Rx';
import {ValidationErrors} from '@angular/forms';
import {ActivationResponse} from '../interfaces/server/activation-response.interface';
import {ExistsResponse} from '../interfaces/server/exists-response.interface';

/**
 * A service used to ask the server questions about barcodes
 *
 * @author Marc Arndt
 */
@Injectable()
export class BarcodeService {

  /**
   * Constructor
   * @param {HttpClient} http A http client
   */
  constructor(private http: HttpClient) {
  }

  /**
   * Checks if the given barcode already exists on the server.
   * If this is the case an [[Observable]] containing a [[ValidationErrors]] is returned,
   * otherwise an [[Observable]] containing `null` is returned
   *
   * @param {string} barcode The string to be tested
   * @returns {Observable<ValidationErrors>} An observable containing the result of the validation
   */
  validateBarcodeExists(barcode: string): Observable<ValidationErrors | null> {
    return this.http
      .get(`/lending-admin-server/rest/barcodes/exists/${barcode}`)
      .map((data: ExistsResponse) => {
        if (data.exists) {
          return {
            barcodeExists: {value: true}
          };
        } else {
          return null;
        }
      });
  }

  /**
   * Checks if the given barcode doesn't exist on the server.
   * If this is the case an [[Observable]] containing a [[ValidationErrors]] is returned,
   * otherwise an [[Observable]] containing `null` is returned
   *
   * @param {string} barcode The string to be tested
   * @returns {Observable<ValidationErrors>} An observable containing the result of the validation
   */
  validateBarcodeNotExists(barcode: string): Observable<ValidationErrors | null> {
    return this.http
      .get(`/lending-admin-server/rest/barcodes/exists/${barcode}`)
      .map((data: ExistsResponse) => {
        if (!data.exists) {
          return {
            barcodeNotExists: {value: true}
          };
        } else {
          return null;
        }
      });
  }

  /**
   * Activates a list of given barcodes. These barcodes can denote either games, identity cards or envelopes
   *
   * @param {Array<string>} barcodes The barcodes of the items to activate
   * @param {(response: ActivationResponse) => void} resultCallback The callback to call with the response from the server
   */
  activateBarcodes(barcodes: Array<string>, resultCallback: (response: ActivationResponse) => void): void {
    this.http
      .post(`/lending-admin-server/rest/barcodes/activate`, barcodes)
      .subscribe(
        (data: ActivationResponse) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback(null)
      );
  }
}
