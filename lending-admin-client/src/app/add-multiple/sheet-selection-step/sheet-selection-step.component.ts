import {Component} from '@angular/core';
import {MultipleAdditionModel} from '../multiple-addition.model';
import {Lendable} from '../../interfaces/server/lendable.interface';

/**
 * A component used to select a sheet inside a workbook, which is specified by a given file
 */
@Component({
  selector: 'lending-sheet-selection-step',
  templateUrl: './sheet-selection-step.component.html',
  styleUrls: ['./sheet-selection-step.component.css']
})
export class SheetSelectionStepComponent {
  constructor(public model: MultipleAdditionModel<Lendable>) {
  }
}
