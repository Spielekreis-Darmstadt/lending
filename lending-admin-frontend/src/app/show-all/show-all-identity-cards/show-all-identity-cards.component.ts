import {Component, OnInit, ViewChild} from '@angular/core';
import {IdentityCard} from '../../interfaces/server/identity-card.interface';
import {IdentityCardService} from '../../core/identity-card.service';
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import {ChangeActivationModalComponent} from "../change-activation-modal/change-activation-modal.component";
import {SearchService} from "../../search.service";

@Component({
  selector: 'lending-show-all-identity-cards',
  templateUrl: './show-all-identity-cards.component.html',
  styleUrls: ['./show-all-identity-cards.component.css']
})
export class ShowAllIdentityCardsComponent implements OnInit {
  public displayedColumns: string[] = ['barcode', 'activated'];

  public dataSource = new MatTableDataSource<IdentityCard>();

  @ViewChild(MatSort, { static: true })
  public sort: MatSort;

  @ViewChild(MatPaginator, { static: true })
  public paginator: MatPaginator;

  constructor(private dialog: MatDialog, private identityCardService: IdentityCardService, private searchService: SearchService) {
    identityCardService.selectIdentityCards(identityCards => this.dataSource.data = identityCards);
  }

  ngOnInit() {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.dataSource.filter = this.searchService.searchTerm;

    this.searchService.searchTermSubject
      .subscribe(searchTerm => this.dataSource.filter = searchTerm);
  }

  /**
   * Selects the given identity card.
   * This opens a modal where the user can activate/deactivate the selected identity card
   *
   * @param identityCard The selected identity card
   */
  selectIdentityCard(identityCard: IdentityCard): void {
    this.dialog.open(ChangeActivationModalComponent, {
      data: {
        entityName: "Ausweis",
        entity: identityCard
      }
    });
  }
}
