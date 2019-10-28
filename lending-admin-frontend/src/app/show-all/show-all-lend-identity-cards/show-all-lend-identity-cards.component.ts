import {Component, OnInit, ViewChild} from '@angular/core';
import {MatDialog, MatPaginator, MatSort, MatTableDataSource} from "@angular/material";
import {LendIdentityCard} from "../../interfaces/server/lend-identity-card.interface";
import {LendIdentityCardService} from "../../core/lend-identity-card.service";
import {ChangeLendIdentityCardModalComponent} from "../change-lend-identity-card-modal/change-lend-identity-card-modal.component";
import {SearchService} from "../../search.service";

@Component({
  selector: 'lending-show-all-lend-identity-cards',
  templateUrl: './show-all-lend-identity-cards.component.html',
  styleUrls: ['./show-all-lend-identity-cards.component.css']
})
export class ShowAllLendIdentityCardsComponent implements OnInit {
  public displayedColumns: string[] = ['identityCardBarcode', 'envelopeBarcode', 'lendTime', 'numberOfLendGames', 'owner'];

  public dataSource = new MatTableDataSource<LendIdentityCard>();

  @ViewChild(MatSort, { static: true })
  public sort: MatSort;

  @ViewChild(MatPaginator, { static: true })
  public paginator: MatPaginator;

  constructor(private dialog: MatDialog, private lendIdentityCardService: LendIdentityCardService, private searchService: SearchService) {
    lendIdentityCardService.selectAllLendIdentityCards(lendIdentityCards => this.dataSource.data = lendIdentityCards);
  }

  ngOnInit() {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.dataSource.filter = this.searchService.searchTerm;

    this.searchService.searchTermSubject
      .subscribe(searchTerm => this.dataSource.filter = searchTerm);
  }

  /**
   * Selects a given lend identity card.
   * This will open a dialog, where the user can change the owner of the identity card
   *
   * @param lid The to be selected lend identity card
   */
  selectLendIdentityCard(lid: LendIdentityCard): void {
    this.dialog.open(ChangeLendIdentityCardModalComponent, {
      width: '500px',
      data: {lendIdentityCard: lid}
    });
  }
}
