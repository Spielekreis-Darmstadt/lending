import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {LendIdentityCard} from '../interfaces/server/lend-identity-card.interface';

@Injectable()
export class LendIdentityCardService {

  /**
   * Constructor
   * @param {HttpClient} http A http client
   */
  constructor(private http: HttpClient) {
  }

  selectAllLendIdentityCards(resultCallback: (result: Array<LendIdentityCard>) => void): void {
    this.http
      .get('/lending-admin-server/rest/lend/identity-cards/all')
      .subscribe(
        (data: Array<LendIdentityCard>) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback([])
      );
  }
}
