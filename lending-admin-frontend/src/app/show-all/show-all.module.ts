import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ShowAllGamesComponent} from './show-all-games/show-all-games.component';
import {SharedModule} from '../shared/shared.module';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import {ShowAllIdentityCardsComponent} from './show-all-identity-cards/show-all-identity-cards.component';
import {ShowAllEnvelopesComponent} from './show-all-envelopes/show-all-envelopes.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    Ng2SmartTableModule
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
export class ShowAllModule { }
