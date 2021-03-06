import { Component, OnInit } from '@angular/core';
import { MultipleActivationModel } from '../multiple-activation.model';

/**
 * A component used to select/drop a file
 */
@Component({
  selector: 'activation-file-selection-step',
  templateUrl: './file-selection-step.component.html',
  styleUrls: ['./file-selection-step.component.scss']
})
export class FileSelectionStepComponent implements OnInit {
  public allowedFileExtensions = ['xlsx', 'xls', 'csv', 'ods'];

  constructor(public model: MultipleActivationModel) {
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

  selectFiles(event): void {
    this.changeFiles(event.target.files);
  }
}
