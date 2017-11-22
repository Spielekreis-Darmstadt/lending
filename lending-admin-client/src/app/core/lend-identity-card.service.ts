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
      .get('/lending-admin-server/rest/lend/identity-cards/all')
      .subscribe(
        (data: Array<LendIdentityCard>) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback([])
      );
  }
}
