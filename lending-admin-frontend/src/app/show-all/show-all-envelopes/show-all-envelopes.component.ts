import {Component, OnInit, ViewChild} from '@angular/core';
import {Envelope} from '../../interfaces/server/envelope.interface';
import {EnvelopeService} from '../../core/envelope.service';
import {MatDialog, MatPaginator, MatTableDataSource} from "@angular/material";
import {ChangeActivationModalComponent} from "../change-activation-modal/change-activation-modal.component";

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

  constructor(private dialog: MatDialog, private envelopeService: EnvelopeService) {
    envelopeService.selectEnvelopes(envelopes => this.dataSource.data = envelopes);
  }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
  }

  /**
   * Selects the given envelope.
   * This opens a modal where the user can activate/deactivate the selected envelope
   *
   * @param envelope The selected envelope
   */
  selectEnvelope(envelope: Envelope): void {
    this.dialog.open(ChangeActivationModalComponent, {
      data: {
        entityName: "Umschlag",
        entity: envelope
      }
    });
  }
}
