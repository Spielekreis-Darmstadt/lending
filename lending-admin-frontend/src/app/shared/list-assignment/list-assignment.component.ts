import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { MatSelectChange } from '@angular/material';

export interface AssignmentDestination {
  /**
   * The displayed title for the database column
   */
  title: string;

  /**
   * A boolean describing if this database column can be assigned multiple times
   */
  multiple: boolean;
}

@Component({
  selector: 'lending-list-assignment',
  templateUrl: './list-assignment.component.html',
  styleUrls: ['./list-assignment.component.css']
})
export class ListAssignmentComponent implements OnChanges {
  /**
   * The table header text for the source column
   */
  @Input()
  public sourceHeader: string;

  /**
   * The table header text for the destination column
   */
  @Input()
  public destinationHeader: string;

  /**
   * The table header text for the available column
   */
  @Input()
  public availableHeader: string;

  /**
   * The text for the default choice, i.e. if the destination value is `null`
   */
  @Input()
  public defaultChoiceText: string;

  /**
   * The source values, to which an assignment is searched
   */
  @Input()
  public source: Array<string> = [];

  /**
   * All assignment choices
   */
  @Input()
  public all: Array<AssignmentDestination> = [];

  /**
   * A callback, which is called when some element has been assigned
   */
  @Output()
  public onDrop: EventEmitter<Array<AssignmentDestination>> = new EventEmitter();

  /**
   * The destination array, containing the made assignments
   */
  public destination: Array<AssignmentDestination> = [];

  /**
   * An array containing all still available and therefore possible database columns
   */
  public get available(): Array<AssignmentDestination> {
    return this.all.filter(value => {
      return !this.destination.includes(value) || value.multiple;
    });
  }

  /**
   * An array of numbers containing an index number interval [0, max(fileheader.length, possibleDatabaseHeader.length)]
   *
   * @returns The available indicies of the assignable items
   */
  public get indices(): Array<number> {
    return Array(this.source.length).fill(0).map((x, i) => i);
  }

  constructor() { }

  public ngOnChanges(changes: SimpleChanges): void {
    if (changes.hasOwnProperty('source')) {
      this.reset();
    }
  }
  /**
   * Changes the value at the given column index `column` to the value contained in `value.dropData`
   *
   * @param column The column index, whose column should be changed
   * @param value The data to be set to the given column
   */
  public changeDropValue(column: number, event: MatSelectChange): void {
    this.destination[column] = event.value;

    this.onDrop.emit(this.destination);
  }

  public reset(): void {
    this.destination = new Array(this.source.length);
  }
}
