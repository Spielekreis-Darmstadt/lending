import {Component, OnInit} from '@angular/core';
import {MultipleAdditionModel} from '../multiple-addition.model';
import {Lendable} from '../../interfaces/server/lendable.interface';

@Component({
  selector: 'addition-summary-step',
  templateUrl: './summary-step.component.html',
  styleUrls: ['./summary-step.component.scss']
})
export class SummaryStepComponent implements OnInit {
  constructor(public model: MultipleAdditionModel<Lendable>) { }

  ngOnInit() {
  }

  public reset(): void {
    this.model.reset();
  }
}
