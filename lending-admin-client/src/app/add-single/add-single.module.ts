import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {AddSingleGameComponent} from './add-singe-game/add-single-game.component';
import {FormsModule} from '@angular/forms';
import {SharedModule} from '../shared/shared.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    SharedModule
  ],
  declarations: [AddSingleGameComponent],
  exports: [AddSingleGameComponent]
})
export class AddSingleModule { }
