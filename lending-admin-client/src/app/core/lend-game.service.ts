import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {LendIdentityCardGroup} from '../interfaces/server/lend-identity-card-group.interface';

@Injectable()
export class LendGameService {

  /**
   * Constructor
   * @param {HttpClient} http A http client
   */
  constructor(private http: HttpClient) {
  }

  selectAllLendGames(resultCallback: (result: Array<LendIdentityCardGroup>) => void): void {
    this.http
      .get('/lending-admin-server/rest/lend/games/all')
      .subscribe(
        (data: Array<LendIdentityCardGroup>) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback([])
      );
  }
}
