import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {LendIdentityCardGroup} from '../interfaces/server/lend-identity-card-group.interface';

/**
 * A service used to process borrowed game related queries
 *
 * @author Marc Arndt
 */
@Injectable()
export class LendGameService {

  /**
   * Constructor
   * @param {HttpClient} http A http client
   */
  constructor(private http: HttpClient) {
  }

  /**
   * Selects all currently lend games, grouped by their identity card barcode, from the server
   *
   * @param {(result: Array<LendIdentityCardGroup>) => void} resultCallback The callback to be called with the currently lend game groups
   */
  selectAllLendGames(resultCallback: (result: Array<LendIdentityCardGroup>) => void): void {
    this.http
      .get('/lending-admin-server/rest/lend/games/all')
      .subscribe(
        (data: Array<LendIdentityCardGroup>) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback([])
      );
  }
}
