import {Injectable} from '@angular/core';
import {Subject} from 'rxjs/Subject';
import {Subscription} from 'rxjs/Subscription';
import {Observable} from 'rxjs/Observable';
import {Observer} from 'rxjs/Observer';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/filter';
import 'rxjs/add/observable/dom/webSocket';
import {Router} from '@angular/router';

@Injectable()
export class OverviewService {
  private subject: Subject<MessageEvent>;

  public gamesOverviewData: Array<any> = [
    {data: [ 0 ], label: 'Alle Spiele'},
    {data: [ 0 ], label: 'Verliehene Spiele'}
  ];

  public identityCardsOverviewData: Array<any> = [
    {data: [ 0 ], label: 'Alle Ausweise'},
    {data: [ 0 ], label: 'Vergebene Ausweise'}
  ];

  constructor() {
    console.log(`creating websocket to endpoint: ws://${window.location.host}/lending-admin-server/overview`);

    Observable.webSocket(`ws://${window.location.host}/lending-admin-server/overview`)
      .subscribe((overviewUpdate: any) => {
        this.gamesOverviewData = [
          {data: [overviewUpdate.numberOfGames], label: 'Alle Spiele'},
          {data: [overviewUpdate.numberOfLendGames], label: 'Verliehene Spiele'}
        ];

        this.identityCardsOverviewData = [
          {data: [overviewUpdate.numberOfIdentityCards], label: 'Alle Ausweise'},
          {data: [overviewUpdate.numberOfLendIdentityCards], label: 'Vergebene Ausweise'}
        ];
      });
  }
}
