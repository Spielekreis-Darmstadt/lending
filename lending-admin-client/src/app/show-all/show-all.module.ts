import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ShowAllGamesComponent} from './show-all-games/show-all-games.component';
import {SharedModule} from '../shared/shared.module';
import {Ng2SmartTableModule} from 'ng2-smart-table';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    Ng2SmartTableModule
  ],
  declarations: [ShowAllGamesComponent],
  exports: [ShowAllGamesComponent]
})
export class ShowAllModule { }
