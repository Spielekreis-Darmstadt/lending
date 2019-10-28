import {Component} from '@angular/core';
import {MultipleAdditionModel} from '../multiple-addition.model';
import {Lendable} from '../../interfaces/server/lendable.interface';

/**
 * A component used to select a sheet inside a workbook, which is specified by a given file
 */
@Component({
  selector: 'addition-sheet-selection-step',
  templateUrl: './sheet-selection-step.component.html',
  styleUrls: ['./sheet-selection-step.component.scss']
})
export class SheetSelectionStepComponent {
  constructor(public model: MultipleAdditionModel<Lendable>) {
  }
}
