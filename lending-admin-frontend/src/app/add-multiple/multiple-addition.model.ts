import {VerificationResult} from '../interfaces/verification-result.interface';
import * as XLSX from 'xlsx';
import {WorkBook} from 'xlsx';
import {InsertionResult} from '../interfaces/insertion-result.interface';
import {Lendable} from '../interfaces/server/lendable.interface';
import {AssignmentDestination} from '../shared/list-assignment/list-assignment.component';

/**
 * An interface used to describe all required fields for an assignable database column
 *
 * @author Marc Arndt
 */
export interface DatabaseColumn<T> extends AssignmentDestination {
  /**
   * A boolean describing if this database column is required
   */
  required: boolean;

  /**
   * A converter function used to add the content of `value` to a given entity `entity`
   *
   * @param {any} value The content to be assigned
   * @param {T} entity The value to which the content should be assigned
   */
  convert(value: any, entity: T): void;
}

/**
 * An abstract model class used for adding multiple items taken from a table file to the database
 *
 * @type ItemType The type of items to be added to the database
 * @author Marc Arndt
 */
export abstract class MultipleAdditionModel<ItemType extends Lendable> {
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
   * An array containing all sheetnames inside the loaded `workbook`
   */
  public sheetNames: Array<string> = [];

  //
  // step 3
  //

  /**
   * The table content of the selected sheet
   */
  public data: Array<Array<string>> = [];

  /**
   * The table header of the selected sheet
   */
  public fileHeader: Array<string> = [];

  /**
   * The content (exluding the table header) of the selected sheet
   */
  public fileContent: Array<Array<string>> = [];

  //
  // step 4
  //

  /**
   * An array of parsed items.
   * These items can be either games, identity cards or envelopes
   */
  public items: Array<ItemType> = [];

  /**
   * An array containing all assigned database columns.
   * This always has to have the same length as `fileHeader`.
   * If a value is unassigned it is either `null` or `undefined`
   */
  public databaseHeader: Array<DatabaseColumn<ItemType>> = [];

  /**
   * An array containing all available database columns
   */
  public abstract readonly allDatabaseHeaders: Array<DatabaseColumn<ItemType>>;

  //
  // step 5
  //

  /**
   * An array containing all column definitions required for the handsontable in the confirmation step
   */
  public abstract readonly columns: Array<any>;

  /**
   * An array containing the column names for the handsontable in the confirmation step
   */
  public abstract readonly columnHeaders: Array<string>;

  /**
   * An object containing the configuration for the handsontable context menu in the confirmation step
   */
  public abstract readonly contextMenuItems: object;

  /**
   * True if all entered items should be activated during insertion, false otherwise
   */
  public activateItems = false;

  /**
   * The server result of the verification of the items inside the handsontable instance
   */
  public verificationResult: VerificationResult = {verified: false};

  //
  // step 6
  //

  /**
   * The insertion response from the server after the items have been inserted
   */
  public insertionResult: InsertionResult;

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
    this.data = <Array<Array<string>>>XLSX.utils.sheet_to_json(sheet, {raw: false, header: 1});
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

  /**
   * Resets all fields in the model
   */
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

    this.activateItems = false;
    this.verificationResult = {verified: false};
    this.insertionResult = null;
  }

  /**
   * Inserts a given list of items in the database
   *
   * @param items The items to be inserted
   */
  public abstract insertItems(items: Array<ItemType>): void;

  /**
   * A function used to verify a given array of items, both on the client and on the server side
   *
   * @param items The items to be verified
   */
  public abstract verifyItems(items: Array<ItemType>): void;
}
