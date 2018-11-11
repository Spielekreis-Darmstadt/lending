import {Component, OnInit, ViewChild} from '@angular/core';
import {IdentityCard} from '../../interfaces/server/identity-card.interface';
import {IdentityCardService} from '../../core/identity-card.service';
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {Game} from "../../interfaces/server/game.interface";

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

  constructor(private identityCardService: IdentityCardService) {
    identityCardService.selectIdentityCards(identityCards => this.dataSource.data = identityCards);
  }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
  }
}
