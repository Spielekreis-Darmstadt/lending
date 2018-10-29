import {Component, OnInit} from '@angular/core';
import {MultipleActivationModel} from '../multiple-activation.model';

@Component({
  selector: 'activation-summary-step',
  templateUrl: './summary-step.component.html',
  styleUrls: ['./summary-step.component.css']
})
export class SummaryStepComponent implements OnInit {
  constructor(public model: MultipleActivationModel) { }

  ngOnInit() {
  }

  public reset(): void {
    this.model.reset();
  }
}
