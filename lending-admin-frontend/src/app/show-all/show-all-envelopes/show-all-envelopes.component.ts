import {Component, OnInit} from '@angular/core';
import {Envelope} from '../../interfaces/server/envelope.interface';
import {EnvelopeService} from '../../core/envelope.service';

@Component({
  selector: 'lending-show-all-envelopes',
  templateUrl: './show-all-envelopes.component.html',
  styleUrls: ['./show-all-envelopes.component.css']
})
export class ShowAllEnvelopesComponent implements OnInit {
  public data: Array<Envelope>;

  public displayedColumns: string[] = ['barcode', 'activated'];

  constructor(private envelopeService: EnvelopeService) {
    envelopeService.selectEnvelopes(envelopes => this.data = envelopes);
  }

  ngOnInit() {
  }
}
