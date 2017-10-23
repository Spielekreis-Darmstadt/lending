import {Component} from '@angular/core';
import {DragDropData} from 'ng2-dnd';
import {DatabaseColumn, MultipleAdditionModel} from '../multiple-addition.model';
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
  constructor(public model: MultipleAdditionModel<Lendable>) {
  }

  public processAssignment(assignment: Array<DatabaseColumn<Lendable>>) {
    this.model.databaseHeader = assignment;
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
}
