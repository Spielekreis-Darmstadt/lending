import { Component, OnInit } from '@angular/core';
import {IdentityCard} from '../../interfaces/server/identity-card.interface';
import {IdentityCardService} from '../../core/identity-card.service';

@Component({
  selector: 'lending-show-all-identity-cards',
  templateUrl: './show-all-identity-cards.component.html',
  styleUrls: ['./show-all-identity-cards.component.css']
})
export class ShowAllIdentityCardsComponent implements OnInit {
  public data: Array<IdentityCard>;

  public settings = {
    actions: false,
    pager: {
      perPage: 30
    },
    columns: {
      barcode: {
        title: 'Barcode'
      },
      activated: {
        title: 'Aktiviert',
        valuePrepareFunction: (activated) => {
          if (activated) {
            return "Ja";
          } else {
            return "Nein"
          }
        },
        filter: {
          type: 'list',
          config: {
            selectText: 'Wähle...',
            list: [
              { value: true, title: "Ja" },
              { value: false, title: "Nein" }
            ]
          }
        }
      }
    }
  };

  constructor(private identityCardService: IdentityCardService) {
    identityCardService.selectIdentityCards(identityCards => this.data = identityCards);
  }

  ngOnInit() {
  }
}
