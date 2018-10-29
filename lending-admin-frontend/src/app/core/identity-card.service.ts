import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {IdentityCard} from '../interfaces/server/identity-card.interface';
import {AddIdentityCardsResponse} from '../interfaces/server/add-identity-cards-response.interface';
import {VerifyIdentityCardsResponse} from '../interfaces/server/verify-identity-cards-response.interface';

/**
 * A service used to do identity card related interaction with the server
 *
 * @author Marc Arndt
 */
@Injectable()
export class IdentityCardService {

  /**
   * Constructor
   * @param {HttpClient} http A http client
   */
  constructor(private http: HttpClient) {
  }

  /**
   * Tries to add a given identity card `identityCard` on the server.
   * Afterwards the given callback `resultCallback` is called.
   * If the identity card addition succeeded the callback is called with `true`, otherwise with `false`.
   * @param {IdentityCard} identityCard The identity card to be added
   * @param {(AddIdentityCardsResponse) => void} resultCallback The callback to be called afterwards
   */
  addIdentityCard(identityCard: IdentityCard, resultCallback: (result: AddIdentityCardsResponse) => void): void {
    this.http
      .put('/lending-admin-backend/rest/identity-cards/add', [ identityCard ])
      .subscribe(
        (data: AddIdentityCardsResponse) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback(null)
      );
  }

  addIdentityCards(identityCards: Array<IdentityCard>, resultCallback: (result: AddIdentityCardsResponse) => void): void {
    this.http
      .put('/lending-admin-backend/rest/identity-cards/add', identityCards)
      .subscribe(
        (data: AddIdentityCardsResponse) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback(null)
      );
  }

  selectIdentityCards(resultCallback: (identityCards: Array<IdentityCard>) => void): void {
    this.http
      .get('/lending-admin-backend/rest/identity-cards/all')
      .subscribe(
        (data: Array<IdentityCard>) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback([])
      );
  }

  verifyIdentityCards(identityCards: Array<IdentityCard>, resultCallback: (valid: VerifyIdentityCardsResponse) => void): void {
    this.http
      .put('/lending-admin-backend/rest/identity-cards/verify', identityCards)
      .subscribe(
        (data: VerifyIdentityCardsResponse) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback(null)
      );
  }
}
