import {Component, EventEmitter, OnInit, Output} from '@angular/core';

/**
 * A component used to select/drop a file
 */
@Component({
  selector: 'lending-file-selection-step',
  templateUrl: './file-selection-step.component.html',
  styleUrls: ['./file-selection-step.component.css']
})
export class FileSelectionStepComponent implements OnInit {
  /**
   * The selected/dropped file
   */
  public selectedFile: File;

  /**
   * An [[EventEmitter]] used to inform a parent component of the selected/dropped file after the step has been completed
   * @type {EventEmitter<any>}
   */
  @Output()
  public onFileSelection: EventEmitter<File> = new EventEmitter();

  constructor() {
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
      this.selectedFile = files[0];
    }
  }

  /**
   * Finishes the step.
   * This action includes a call of the [[EventEmitter]] `onFileSelection`, which informs a parent component of the selected/dropped file
   */
  finishStep(): void {
    this.onFileSelection.emit(this.selectedFile);
  }
}
