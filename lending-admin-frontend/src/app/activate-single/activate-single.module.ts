import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivateSingleGameComponent} from './activate-single-game/activate-single-game.component';
import {FormsModule} from '@angular/forms';
import {SharedModule} from '../shared/shared.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    SharedModule
  ],
  declarations: [ActivateSingleGameComponent],
  exports: [ActivateSingleGameComponent]
})
export class ActivateSingleModule {
}
