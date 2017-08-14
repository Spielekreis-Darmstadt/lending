import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

/**
 * A component used to show the user the content of a given table, represented by a two dimensional array
 */
@Component({
  selector: 'lending-check-sheet-content-step',
  templateUrl: './check-sheet-content-step.component.html',
  styleUrls: ['./check-sheet-content-step.component.css']
})
export class CheckSheetContentStepComponent implements OnInit {
  /**
   * An array containing the content of a table, including the header row
   */
  @Input()
  public data: Array<Array<string>>;

  /**
   * An [[EventEmitter]] used to inform a parent component about the contained header row
   * and the data content of the `data` array
   *
   * @type {EventEmitter<any>}
   */
  @Output()
  public onContentChecked: EventEmitter<{ header: Array<string>, data: Array<Array<string>> }> = new EventEmitter();

  constructor() {
  }

  ngOnInit() {
  }

  /**
   * Complete the step.
   * This action contains a call of the [[EventEmitter]] `onContentChecked`
   * to inform a parent component of the header row and the data content of the data table `data`
   */
  finishStep(): void {
    if (this.data && this.data.length > 0) {
      const header = this.data[0];
      const content = this.data.slice(1);

      this.onContentChecked.emit({header: header, data: content});
    } else {
      this.onContentChecked.emit({header: [], data: []});
    }
  }
}
