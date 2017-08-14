import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {DragDropData} from "ng2-dnd";

/**
 * An interface used to describe all required fields for an assignable database column
 */
export interface DatabaseColumn<T> {
  /**
   * The displayed title for the database column
   */
  title: string;

  /**
   * A boolean describing if this database column is required
   */
  required: boolean;

  /**
   * A boolean describing if this database column can be assigned multiple times
   */
  multiple: boolean;

  /**
   * A converter function used to add the content of `value` to a given entity `entity`
   *
   * @param {any} value The content to be assigned
   * @param {T} entity The value to which the content should be assigned
   */
  convert(value: any, entity: T): void;
}

/**
 * A component used to assign a list of database columns to a list of rows in a table
 */
@Component({
  selector: 'lending-column-assignment-step',
  templateUrl: './column-assignment-step.component.html',
  styleUrls: ['./column-assignment-step.component.css']
})
export class ColumnAssignmentStepComponent implements OnChanges {
  /**
   * An array of strings containing the header row of the excel file
   */
  @Input()
  public fileHeader: Array<string>;

  /**
   * An array containing the data inside the excel file.
   * This array doesn't contain the header row!
   */
  @Input()
  public fileData: Array<Array<string>>;

  /**
   * An array containing all possible database columns, the user can possibly assign
   */
  @Input()
  public allDatabaseHeader: Array<DatabaseColumn<any>>;

  /**
   * An [[EventEmitter]] used to inform a parent component of the result of the column assignment
   * This [[EventEmitter]] is used after the user has pressed the `Next` button
   *
   * @type {EventEmitter<any>}
   */
  @Output()
  public onColumnAssigned: EventEmitter<Array<any>> = new EventEmitter();

  /**
   * An array containing all assigned database columns.
   * This always has to have the same length as `fileHeader`.
   * If a value is unassigned it is either null or undefined
   *
   * @type {Array}
   */
  public databaseHeader: Array<DatabaseColumn<any>> = [];

  /**
   * An array containing all still available and therefore possible database columns
   *
   * @returns {Array<DatabaseColumn<any>>}
   */
  public get possibleDatabaseHeader(): Array<DatabaseColumn<any>> {
    return this.allDatabaseHeader.filter(value => {
      return !this.databaseHeader.includes(value) || value.multiple;
    });
  }

  /**
   * An array of numbers containing an index number interval [0, max(fileheader.length, possibleDatabaseHeader.length)]
   *
   * @returns {Array<number>}
   */
  public get indices(): Array<number> {
    return Array(Math.max(this.fileHeader.length, this.possibleDatabaseHeader.length)).fill(0).map((x, i) => i);
  }

  constructor() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    // adapt the size of the databaseHeader array to the changed fileHeader array
    if (changes.hasOwnProperty('fileHeader') && this.fileHeader) {
      this.databaseHeader = new Array(this.fileHeader.length);
    }
  }

  /**
   * Changes the value at the given column index `column` to the value contained in `value.dropData`
   *
   * @param {number} column The column index, whose column should be changed
   * @param {DragDropData} value The data to be set to the given column
   */
  changeDropValue(column: number, value: DragDropData): void {
    this.databaseHeader[column] = value.dragData;
  }

  /**
   * Checks, whether the step can be exited/finished.
   * This is the case if all database columns are assigned, that are marked as `required`
   *
   * @returns {boolean} True if this step can be exited, false otherwise
   */
  canFinish(): boolean {
    return this.allDatabaseHeader
      .filter(header => header.required)
      .every(header => this.databaseHeader.includes(header));
  }

  /**
   * Complete the step.
   * This action includes the usage of the made assignments to create entity objects,
   * which are then passed to the `onColumnAssigned` callback
   */
  finishStep(): void {
    const entities = this.fileData.map((entry: Array<string>) => {
      const result = {};

      for (let index = 0; index < entry.length; index++) {
        if (this.databaseHeader[index]) {
          this.databaseHeader[index].convert(entry[index], result);
        }
      }

      return result;
    });

    this.onColumnAssigned.emit(entities);
  }

  /**
   * Resets the assignments made by the user (i.e. it empties the assigned database column array)
   */
  resetInput(): void {
    this.databaseHeader = new Array(this.fileHeader.length);
  }
}
