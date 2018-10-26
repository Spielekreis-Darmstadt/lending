import { Component, OnInit } from '@angular/core';
import {Envelope} from '../../interfaces/server/envelope.interface';
import {EnvelopeService} from '../../core/envelope.service';

@Component({
  selector: 'lending-show-all-envelopes',
  templateUrl: './show-all-envelopes.component.html',
  styleUrls: ['./show-all-envelopes.component.css']
})
export class ShowAllEnvelopesComponent implements OnInit {
  public data: Array<Envelope>;

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

  constructor(private envelopeService: EnvelopeService) {
    envelopeService.selectEnvelopes(envelopes => this.data = envelopes);
  }

  ngOnInit() {
  }
}
