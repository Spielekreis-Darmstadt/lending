import {Component, OnInit} from '@angular/core';
import {MultipleAdditionModel} from '../multiple-addition.model';
import {Lendable} from '../../interfaces/server/lendable.interface';

/**
 * A component used to show the user the content of a given table, represented by a two dimensional array
 */
@Component({
  selector: 'lending-check-sheet-content-step',
  templateUrl: './check-sheet-content-step.component.html',
  styleUrls: ['./check-sheet-content-step.component.css']
})
export class CheckSheetContentStepComponent implements OnInit {
  constructor(public model: MultipleAdditionModel<Lendable>) {
  }

  ngOnInit() {
  }
}
