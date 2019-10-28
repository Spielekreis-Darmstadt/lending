import { Component } from '@angular/core';
import { DatabaseColumn, MultipleActivationModel } from '../multiple-activation.model';

/**
 * A component used to assign a list of database columns to a list of rows in a table
 */
@Component({
  selector: 'activation-column-assignment-step',
  templateUrl: './column-assignment-step.component.html',
  styleUrls: ['./column-assignment-step.component.scss']
})
export class ColumnAssignmentStepComponent {
  constructor(public model: MultipleActivationModel) {
  }

  public processAssignment(assignment: Array<DatabaseColumn>) {
    this.model.databaseHeader = assignment;
  }

  /**
   * Checks, whether the step can be exited/finished.
   * This is the case if all database columns are assigned, that are marked as `required`
   *
   * @returns True if this step can be exited, false otherwise
   */
  canFinish(): boolean {
    return this.model.allDatabaseHeaders
      .filter(header => header.required)
      .every(header => this.model.databaseHeader.includes(header));
  }
}
