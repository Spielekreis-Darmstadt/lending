import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ShowAllGamesComponent} from './show-all-games/show-all-games.component';
import {ShowAllIdentityCardsComponent} from './show-all-identity-cards/show-all-identity-cards.component';
import {ShowAllEnvelopesComponent} from './show-all-envelopes/show-all-envelopes.component';
import {SharedModule} from '../shared/shared.module';
import {MatPaginatorModule, MatTableModule} from '@angular/material';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    MatTableModule,
    MatPaginatorModule
  ],
  declarations: [
    ShowAllGamesComponent,
    ShowAllIdentityCardsComponent,
    ShowAllEnvelopesComponent
  ],
  exports: [
    ShowAllGamesComponent,
    ShowAllIdentityCardsComponent,
    ShowAllEnvelopesComponent
  ]
})
export class ShowAllModule {
}
