import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ListAssignmentComponent} from '../shared/list-assignment/list-assignment.component';
import {DropZoneDirective} from '../shared/drop-zone/drop-zone.directive';
import {DndModule} from 'ng2-dnd';
import {BarcodeValidatorDirective} from '../shared/barcode-validator/barcode-validator.directive';

@NgModule({
  imports: [
    CommonModule,
    DndModule.forRoot()
  ],
  declarations: [
    ListAssignmentComponent,
    DropZoneDirective,
    BarcodeValidatorDirective
  ],
  exports: [
    ListAssignmentComponent,
    DropZoneDirective,
    BarcodeValidatorDirective
  ]
})
export class SharedModule { }
