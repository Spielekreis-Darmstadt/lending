import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ShowAllGamesComponent} from './show-all-games/show-all-games.component';
import {ShowAllIdentityCardsComponent} from './show-all-identity-cards/show-all-identity-cards.component';
import {ShowAllEnvelopesComponent} from './show-all-envelopes/show-all-envelopes.component';
import {SharedModule} from '../shared/shared.module';
import { MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import {ShowAllLendIdentityCardsComponent} from "./show-all-lend-identity-cards/show-all-lend-identity-cards.component";
import {ChangeLendIdentityCardModalComponent} from "./change-lend-identity-card-modal/change-lend-identity-card-modal.component";
import {FormsModule} from "@angular/forms";
import {ChangeActivationModalComponent} from "./change-activation-modal/change-activation-modal.component";
import {ShowAllLendGamesComponent} from "./show-all-lend-games/show-all-lend-games.component";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    SharedModule,
    MatTableModule,
    MatPaginatorModule,
    MatDialogModule,
    MatSortModule
  ],
  declarations: [
    ShowAllGamesComponent,
    ShowAllIdentityCardsComponent,
    ShowAllEnvelopesComponent,
    ShowAllLendGamesComponent,
    ShowAllLendIdentityCardsComponent,
    ChangeLendIdentityCardModalComponent,
    ChangeActivationModalComponent
  ],
  exports: [
    ShowAllGamesComponent,
    ShowAllIdentityCardsComponent,
    ShowAllEnvelopesComponent,
    ShowAllLendGamesComponent,
    ShowAllLendIdentityCardsComponent
  ],
  entryComponents: [
    ChangeLendIdentityCardModalComponent,
    ChangeActivationModalComponent
  ],
})
export class ShowAllModule {
}
