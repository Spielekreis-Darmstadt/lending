import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AddGamesResponse} from '../interfaces/add-games-response.interface';

@Component({
  selector: 'lending-summary-step',
  templateUrl: './summary-step.component.html',
  styleUrls: ['./summary-step.component.css']
})
export class SummaryStepComponent implements OnInit {
  @Input()
  public addItemsResult: AddGamesResponse;

  @Input()
  public items: Array<any> = [];

  @Output()
  public onReset: EventEmitter<void> = new EventEmitter<void>();

  constructor() { }

  ngOnInit() {
  }

  public reset(): void {
    this.onReset.emit();
  }
}
