import {AddGamesResponse} from './interfaces/add-games-response.interface';
import {VerificationResult} from './interfaces/verification-result.interface';
import * as XLSX from 'xlsx';
import {WorkBook} from 'xlsx';

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

export abstract class MultipleAdditionModel<ItemType> {
  //
  // step 1
  //

  /**
   * The dropped/selected file
   */
  public file: File;

  //
  // step 2
  //

  /**
   * The workbook loaded from `file`
   */
  private workbook: WorkBook;

  /**
   * The selected sheetname
   */
  public selectedSheetName: string;

  /**
   * An array containing all sheetnames inside `workbook`
   * @type {Array}
   */
  public sheetNames: Array<string> = [];

  //
  // step 3
  //

  /**
   * The table content of the selected sheet
   *
   * @type {Array}
   */
  public data: Array<Array<string>> = [];

  /**
   * The table header of the selected sheet
   *
   * @type {Array}
   */
  public fileHeader: Array<string> = [];

  /**
   * The content (exluding the table header) of the selected sheet
   *
   * @type {Array}
   */
  public fileContent: Array<Array<string>> = [];

  //
  // step 4
  //

  public items: Array<ItemType> = [];

  /**
   * An array containing all assigned database columns.
   * This always has to have the same length as `fileHeader`.
   * If a value is unassigned it is either null or undefined
   *
   * @type {Array}
   */
  public databaseHeader: Array<DatabaseColumn<ItemType>> = [];

  /**
   * An array containing all still available and therefore possible database columns
   *
   * @returns {Array<DatabaseColumn<any>>}
   */
  public get possibleDatabaseHeaders(): Array<DatabaseColumn<ItemType>> {
    return this.allDatabaseHeaders.filter(value => {
      return !this.databaseHeader.includes(value) || value.multiple;
    });
  }

  //
  // step 5
  //

  /**
   * The server result of the verification of the items inside the handsontable instance
   *
   * @type {{verified: boolean}}
   */
  public verificationResult: VerificationResult = { verified: false };

  //
  // step 6
  //

  public insertionResult: AddGamesResponse;

  public abstract readonly allDatabaseHeaders: Array<DatabaseColumn<ItemType>>;

  public abstract readonly columns: Array<any>;

  public abstract readonly columnHeaders: Array<string>;

  public abstract readonly verifyItems: (games: Array<ItemType>, callback: (verificationResult: VerificationResult) => void) => void;

  /**
   * Callback for the file-selection step
   *
   * @param {File} file The selected file
   */
  loadWorkbook(file: File): void {
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

    reader.readAsBinaryString(file);

    this.file = file;
  }

  /**
   * Callback for the sheet-selection step
   */
  loadSheet(): void {
    const sheet = this.workbook.Sheets[this.selectedSheetName];
    // convert the sheet content to a two dimensional array
    this.data = <Array<Array<any>>>XLSX.utils.sheet_to_json(sheet, {header: 1});
  }

  /**
   * Callback for the check-sheet-content step
   */
  loadTable(): void {
    if (this.data && this.data.length > 0) {
      const header = this.data[0];
      const content = this.data.slice(1);

      this.fileHeader = header;
      this.fileContent = content;
    } else {
      this.fileHeader = [];
      this.fileContent = [];
    }

    this.databaseHeader = new Array(this.fileHeader.length);
  }

  /**
   * Callback for the column-assignment step
   */
  loadAssignments(): void {
    this.items = this.fileContent.map((entry: Array<string>) => {
      const result: any = {};

      for (let index = 0; index < entry.length; index++) {
        if (this.databaseHeader[index]) {
          this.databaseHeader[index].convert(entry[index], result);
        }
      }

      return result;
    });
  }

  reset(): void {
    this.file = null;

    this.workbook = null;
    this.selectedSheetName = null;
    this.sheetNames = [];

    this.data = [];
    this.fileHeader = [];
    this.fileContent = [];

    this.items = [];
    this.databaseHeader = [];

    this.verificationResult = { verified: false };
    this.insertionResult = null;
  }

  public abstract insertItems(items: Array<ItemType>): void;
}
