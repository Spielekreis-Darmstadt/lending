import {Injectable} from '@angular/core';
import {webSocket} from 'rxjs/webSocket';

@Injectable()
export class OverviewService {
  public gamesOverviewData: Array<any> =
    [{
      name: 'Alle Spiele',
      value: 0
    }, {
      name: 'Verliehene Spiele',
      value: 0
    }];

  public identityCardsOverviewData: Array<any> =
    [{
      name: 'Alle Ausweise',
      value: 0
    }, {
      name: 'Ausgegebene Ausweise',
      value: 0
    }];

  constructor() {
    console.log(`creating websocket to endpoint: ws://${window.location.host}/lending-admin-backend/overview`);

    webSocket(`ws://${window.location.host}/lending-admin-backend/overview`)
      .subscribe((overviewUpdate: any) => {
        this.gamesOverviewData = [{
          name: 'Alle Spiele',
          value: overviewUpdate.numberOfGames
        }, {
          name: 'Verliehene Spiele',
          value: overviewUpdate.numberOfLendGames
        }];

        this.identityCardsOverviewData = [{
          name: 'Alle Ausweise',
          value: overviewUpdate.numberOfIdentityCards
        }, {
          name: 'Ausgegebene Ausweise',
          value: overviewUpdate.numberOfLendIdentityCards
        }];
      });
  }
}
