import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from 'rxjs/Rx';
import {ValidationErrors} from "@angular/forms";

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
   * @param {string} barcode The string to be tested
   * @returns {Observable<ValidationErrors>} An observable containing the result of the validation
   */
  validateBarcodeExists(barcode: string): Observable<ValidationErrors | null> {
    return this.http
      .get(`/lending-admin-server/rest/barcodes/exists/${barcode}`)
      .map(data => {
        if (data == true) {
          return {"barcodeExists": {value: true}};
        } else {
          return null;
        }
      });
  }
}
