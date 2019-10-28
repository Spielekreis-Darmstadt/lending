import {Component, OnInit} from '@angular/core';
import {MultipleActivationModel} from '../multiple-activation.model';

/**
 * A component used to show the user the content of a given table, represented by a two dimensional array
 */
@Component({
  selector: 'activation-check-sheet-content-step',
  templateUrl: './check-sheet-content-step.component.html',
  styleUrls: ['./check-sheet-content-step.component.scss']
})
export class CheckSheetContentStepComponent implements OnInit {
  constructor(public model: MultipleActivationModel) {
  }

  ngOnInit() {
  }
}
