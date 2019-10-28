import { Component } from '@angular/core';
import { MultipleActivationModel } from '../multiple-activation.model';

/**
 * A component used to select a sheet inside a workbook, which is specified by a given file
 */
@Component({
  selector: 'activation-sheet-selection-step',
  templateUrl: './sheet-selection-step.component.html',
  styleUrls: ['./sheet-selection-step.component.scss']
})
export class SheetSelectionStepComponent {
  constructor(public model: MultipleActivationModel) {
  }
}
