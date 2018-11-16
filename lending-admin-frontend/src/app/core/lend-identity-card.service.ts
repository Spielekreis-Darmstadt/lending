import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {LendIdentityCard} from '../interfaces/server/lend-identity-card.interface';

/**
 * A service class, used to process issued identity card related queries
 *
 * @author Marc Arndt
 */
@Injectable()
export class LendIdentityCardService {

  /**
   * Constructor
   *
   * @param {HttpClient} http A http client
   */
  constructor(private http: HttpClient) {
  }

  /**
   * Select all currently issued identity cards from the server
   *
   * @param {(result: Array<LendIdentityCard>) => void} resultCallback The callback to be called with the currently issued identity cards
   */
  selectAllLendIdentityCards(resultCallback: (result: Array<LendIdentityCard>) => void): void {
    this.http
      .get('/lending-admin-backend/rest/lend/identity-cards/all')
      .subscribe(
        (data: Array<LendIdentityCard>) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback([])
      );
  }

  /**
   * Changes the owner of the identity card given by its barcode to the given owner.
   * Afterwards the result callback is called
   *
   * @param identityCardBarcode The barcode of the identity card
   * @param owner The new owner
   * @param resultCallback The result callback
   */
  updateOwner(identityCardBarcode: string, owner: string, resultCallback: (result?: LendIdentityCard) => void): void {
    this.http
      .post('/lending-admin-backend/rest/lend/identity-cards/owner', {
        identityCardBarcodeString: identityCardBarcode,
        owner: owner
      })
      .subscribe(
        (data: LendIdentityCard) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback()
      );
  }

  /**
   * Clears the owner of the identity card given by its barcode.
   * Afterwards the result callback is called
   *
   * @param identityCardBarcode The barcode of the identity card
   * @param resultCallback The result callback
   */
  clearOwner(identityCardBarcode: string, resultCallback: (result?: LendIdentityCard) => void): void {
    this.http
      .post('/lending-admin-backend/rest/lend/identity-cards/owner', {
        identityCardBarcodeString: identityCardBarcode
      })
      .subscribe(
        (data: LendIdentityCard) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback()
      );
  }
}
