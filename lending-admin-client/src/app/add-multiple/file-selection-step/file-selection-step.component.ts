import {Component, OnInit} from '@angular/core';
import {MultipleAdditionModel} from '../multiple-addition.model';
import {Lendable} from '../../interfaces/server/lendable.interface';

/**
 * A component used to select/drop a file
 */
@Component({
  selector: 'lending-file-selection-step',
  templateUrl: './file-selection-step.component.html',
  styleUrls: ['./file-selection-step.component.css']
})
export class FileSelectionStepComponent implements OnInit {
  public allowedFileExtensions = ['xlsx', 'xls', 'csv', 'ods'];

  constructor(public model: MultipleAdditionModel<Lendable>) {
  }

  ngOnInit() {
  }

  /**
   * Changes the selected/dropped file
   *
   * @param {Array<File>} files A list of all dropped files
   */
  changeFiles(files: Array<File>): void {
    if (files && files.length > 0) {
      // use the first file in the array
      this.model.loadWorkbook(files[0]);
    }
  }
}
