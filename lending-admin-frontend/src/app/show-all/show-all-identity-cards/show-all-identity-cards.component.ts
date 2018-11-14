import {Component, OnInit, ViewChild} from '@angular/core';
import {IdentityCard} from '../../interfaces/server/identity-card.interface';
import {IdentityCardService} from '../../core/identity-card.service';
import {MatDialog, MatPaginator, MatTableDataSource} from "@angular/material";
import {ChangeActivationModalComponent} from "../change-activation-modal/change-activation-modal.component";

@Component({
  selector: 'lending-show-all-identity-cards',
  templateUrl: './show-all-identity-cards.component.html',
  styleUrls: ['./show-all-identity-cards.component.css']
})
export class ShowAllIdentityCardsComponent implements OnInit {
  public displayedColumns: string[] = ['barcode', 'activated'];

  public dataSource = new MatTableDataSource<IdentityCard>();

  @ViewChild(MatPaginator)
  public paginator: MatPaginator;

  constructor(private dialog: MatDialog, private identityCardService: IdentityCardService) {
    identityCardService.selectIdentityCards(identityCards => this.dataSource.data = identityCards);
  }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
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
