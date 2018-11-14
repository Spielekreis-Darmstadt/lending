import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ShowAllGamesComponent} from './show-all-games/show-all-games.component';
import {ShowAllIdentityCardsComponent} from './show-all-identity-cards/show-all-identity-cards.component';
import {ShowAllEnvelopesComponent} from './show-all-envelopes/show-all-envelopes.component';
import {SharedModule} from '../shared/shared.module';
import {MatDialogModule, MatPaginatorModule, MatTableModule} from '@angular/material';
import {ShowAllLendIdentityCardsComponent} from "./show-all-lend-identity-cards/show-all-lend-identity-cards.component";
import {ChangeLendIdentityCardModalComponent} from "./change-lend-identity-card-modal/change-lend-identity-card-modal.component";
import {FormsModule} from "@angular/forms";
import {ChangeActivationModalComponent} from "./change-activation-modal/change-activation-modal.component";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    SharedModule,
    MatTableModule,
    MatPaginatorModule,
    MatDialogModule
  ],
  declarations: [
    ShowAllGamesComponent,
    ShowAllIdentityCardsComponent,
    ShowAllEnvelopesComponent,
    ShowAllLendIdentityCardsComponent,
    ChangeLendIdentityCardModalComponent,
    ChangeActivationModalComponent
  ],
  exports: [
    ShowAllGamesComponent,
    ShowAllIdentityCardsComponent,
    ShowAllEnvelopesComponent,
    ShowAllLendIdentityCardsComponent
  ],
  entryComponents: [
    ChangeLendIdentityCardModalComponent,
    ChangeActivationModalComponent
  ],
})
export class ShowAllModule {
}
