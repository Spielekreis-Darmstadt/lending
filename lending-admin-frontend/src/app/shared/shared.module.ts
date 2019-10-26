import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSelectModule } from '@angular/material';
import { BarcodeNotExistValidatorDirective } from './barcode-not-exist-validator/barcode-not-exist-validator.directive';
import { BarcodeExistValidatorDirective } from './barcode-exist-validator/barcode-exist-validator.directive';
import { ListAssignmentComponent } from './list-assignment/list-assignment.component';
import { DropZoneDirective } from './drop-zone/drop-zone.directive';

@NgModule({
  imports: [
    CommonModule,
    MatSelectModule
  ],
  declarations: [
    ListAssignmentComponent,
    DropZoneDirective,
    BarcodeExistValidatorDirective,
    BarcodeNotExistValidatorDirective
  ],
  exports: [
    ListAssignmentComponent,
    DropZoneDirective,
    BarcodeExistValidatorDirective,
    BarcodeNotExistValidatorDirective
  ]
})
export class SharedModule {
}
