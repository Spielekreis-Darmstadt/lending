import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DndModule} from 'ng2-dnd';
import {BarcodeNotExistValidatorDirective} from './barcode-not-exist-validator/barcode-not-exist-validator.directive';
import {BarcodeExistValidatorDirective} from './barcode-exist-validator/barcode-exist-validator.directive';
import {ListAssignmentComponent} from './list-assignment/list-assignment.component';
import {DropZoneDirective} from './drop-zone/drop-zone.directive';

@NgModule({
  imports: [
    CommonModule,
    DndModule.forRoot()
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
