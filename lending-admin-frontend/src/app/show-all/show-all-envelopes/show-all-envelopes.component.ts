import {Component, OnInit, ViewChild} from '@angular/core';
import {Envelope} from '../../interfaces/server/envelope.interface';
import {EnvelopeService} from '../../core/envelope.service';
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import {ChangeActivationModalComponent} from "../change-activation-modal/change-activation-modal.component";
import {SearchService} from "../../search.service";

@Component({
  selector: 'lending-show-all-envelopes',
  templateUrl: './show-all-envelopes.component.html',
  styleUrls: ['./show-all-envelopes.component.css']
})
export class ShowAllEnvelopesComponent implements OnInit {
  public displayedColumns: string[] = ['barcode', 'activated'];

  public dataSource = new MatTableDataSource<Envelope>();

  @ViewChild(MatSort, { static: true })
  public sort: MatSort;

  @ViewChild(MatPaginator, { static: true })
  public paginator: MatPaginator;

  constructor(private dialog: MatDialog, private envelopeService: EnvelopeService, private searchService: SearchService) {
    envelopeService.selectEnvelopes(envelopes => this.dataSource.data = envelopes);
  }

  ngOnInit() {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.dataSource.filter = this.searchService.searchTerm;

    this.searchService.searchTermSubject
      .subscribe(searchTerm => this.dataSource.filter = searchTerm);
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
