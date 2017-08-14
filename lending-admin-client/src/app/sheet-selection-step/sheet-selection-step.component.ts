import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import * as XLSX from "xlsx";
import {WorkBook} from "xlsx";

/**
 * A component used to select a sheet inside a workbook, which is specified by a given file
 */
@Component({
  selector: 'lending-sheet-selection-step',
  templateUrl: './sheet-selection-step.component.html',
  styleUrls: ['./sheet-selection-step.component.css']
})
export class SheetSelectionStepComponent implements OnChanges {
  /**
   * The selected sheetname
   */
  public selectedSheetName: string;

  /**
   * An array containing all sheetnames inside `workbook`
   * @type {Array}
   */
  public sheetNames: Array<string> = [];

  /**
   * The file containing an excel workbook with worksheets
   */
  @Input()
  public file: File;

  /**
   * An [[EventEmitter]] used to inform a parent component of the data included in the selected sheet.
   * This [[EventEmitter]] is called after the user has clicked on the `Next` button
   *
   * @type {EventEmitter<any>}
   */
  @Output()
  public onSheetSelection: EventEmitter<Array<Array<string>>> = new EventEmitter();

  /**
   * The workbook loaded from `file`
   */
  private workbook: WorkBook;

  constructor() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    // the selected file has changed and it is set (i.e. not on initialization)
    if (changes.hasOwnProperty('file') && this.file) {
      const reader = new FileReader();

      reader.onload = (e: any) => {
        const bstr = e.target.result;

        // read the workbook
        this.workbook = XLSX.read(bstr, {type: 'binary'});

        // extract the sheet names contained in the workbook
        this.sheetNames = this.workbook.SheetNames;

        if (this.sheetNames.length > 0) {
          this.selectedSheetName = this.sheetNames[0];
        }
      };

      reader.readAsBinaryString(this.file);
    }
  }

  /**
   * Finishes the step.
   * This action includes the extraction of the data contained on the selected sheet and
   * passing this data to the given [[EventEmitter]] `onSheetSelection`, which informs a parent component
   * of the content of the selected sheet
   */
  finishStep(): void {
    const sheet = this.workbook.Sheets[this.selectedSheetName];
    // convert the sheet content to a two dimensional array
    const data = <Array<Array<any>>>XLSX.utils.sheet_to_json(sheet, {header: 1});

    this.onSheetSelection.emit(data);
  }

}
