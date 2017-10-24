import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {BarcodeService} from './barcode.service';
import {GameService} from './game.service';

@NgModule({
  imports: [
    CommonModule,
  ],
  providers: [
    BarcodeService,
    GameService
  ]
})
export class CoreModule {
}
