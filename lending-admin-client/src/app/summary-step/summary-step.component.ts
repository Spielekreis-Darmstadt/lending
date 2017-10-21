import {Component, OnInit} from '@angular/core';
import {MultipleAdditionModel} from '../multiple-addition.model';
import {Lendable} from '../interfaces/server/lendable.interface';

@Component({
  selector: 'lending-summary-step',
  templateUrl: './summary-step.component.html',
  styleUrls: ['./summary-step.component.css']
})
export class SummaryStepComponent implements OnInit {
  constructor(public model: MultipleAdditionModel<Lendable>) { }

  ngOnInit() {
  }

  public reset(): void {
    this.model.reset();
  }
}
