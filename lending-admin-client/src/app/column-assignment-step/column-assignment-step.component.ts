import {Component} from '@angular/core';
import {DragDropData} from 'ng2-dnd';
import {MultipleAdditionModel} from '../multiple-addition.model';
import {Lendable} from '../interfaces/server/lendable.interface';

/**
 * A component used to assign a list of database columns to a list of rows in a table
 */
@Component({
  selector: 'lending-column-assignment-step',
  templateUrl: './column-assignment-step.component.html',
  styleUrls: ['./column-assignment-step.component.css']
})
export class ColumnAssignmentStepComponent {
  /**
   * An array of numbers containing an index number interval [0, max(fileheader.length, possibleDatabaseHeader.length)]
   *
   * @returns {Array<number>}
   */
  public get indices(): Array<number> {
    return Array(Math.max(this.model.fileHeader.length, this.model.possibleDatabaseHeaders.length)).fill(0).map((x, i) => i);
  }

  constructor(public model: MultipleAdditionModel<Lendable>) {
  }

  /**
   * Changes the value at the given column index `column` to the value contained in `value.dropData`
   *
   * @param {number} column The column index, whose column should be changed
   * @param {DragDropData} value The data to be set to the given column
   */
  changeDropValue(column: number, value: DragDropData): void {
    this.model.databaseHeader[column] = value.dragData;
  }

  /**
   * Checks, whether the step can be exited/finished.
   * This is the case if all database columns are assigned, that are marked as `required`
   *
   * @returns {boolean} True if this step can be exited, false otherwise
   */
  canFinish(): boolean {
    return this.model.allDatabaseHeaders
      .filter(header => header.required)
      .every(header => this.model.databaseHeader.includes(header));
  }

  /**
   * Resets the assignments made by the user (i.e. it empties the assigned database column array)
   */
  resetInput(): void {
    this.model.databaseHeader = new Array(this.model.fileHeader.length);
  }
}
