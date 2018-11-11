import {Component, OnInit, ViewChild} from '@angular/core';
import {Envelope} from '../../interfaces/server/envelope.interface';
import {EnvelopeService} from '../../core/envelope.service';
import {MatPaginator, MatTableDataSource} from "@angular/material";

@Component({
  selector: 'lending-show-all-envelopes',
  templateUrl: './show-all-envelopes.component.html',
  styleUrls: ['./show-all-envelopes.component.css']
})
export class ShowAllEnvelopesComponent implements OnInit {
  public displayedColumns: string[] = ['barcode', 'activated'];

  public dataSource = new MatTableDataSource<Envelope>();

  @ViewChild(MatPaginator)
  public paginator: MatPaginator;

  constructor(private envelopeService: EnvelopeService) {
    envelopeService.selectEnvelopes(envelopes => this.dataSource.data = envelopes);
  }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
  }
}
