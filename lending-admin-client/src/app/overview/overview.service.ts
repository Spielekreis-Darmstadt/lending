import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/filter';
import 'rxjs/add/observable/dom/webSocket';

@Injectable()
export class OverviewService {
  public gamesOverviewData: Array<any> =
    [{
      label: 'Game statistics',
      data: [0, 0],
      backgroundColor: ['red', 'blue']
    }];

  public identityCardsOverviewData: Array<any> =
    [{
      label: 'Identity Card statistics',
      data: [0, 0],
      backgroundColor: ['red', 'blue']
    }];

  constructor() {
    console.log(`creating websocket to endpoint: ws://${window.location.host}/lending-admin-server/overview`);

    Observable.webSocket(`ws://${window.location.host}/lending-admin-server/overview`)
      .subscribe((overviewUpdate: any) => {
        this.gamesOverviewData = [{
          label: 'Game statistics',
          data: [overviewUpdate.numberOfGames, overviewUpdate.numberOfLendGames],
          backgroundColor: ['red', 'blue']
        }];

        this.identityCardsOverviewData = [{
          label: 'Identity Card statistics',
          data: [overviewUpdate.numberOfIdentityCards, overviewUpdate.numberOfLendIdentityCards],
          backgroundColor: ['red', 'blue']
        }];
      });
  }
}
