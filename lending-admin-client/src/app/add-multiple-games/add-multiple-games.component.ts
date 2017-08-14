import {Component, OnInit} from '@angular/core';
import {DatabaseColumn} from "../column-assignment-step/column-assignment-step.component";
import {Game} from "../interfaces/game.interface";

/**
 * A component used to add a list of games at once.
 * This list needs to be provided as a table file, e.g. as an xls or csv file
 */
@Component({
  selector: 'lending-add-multiple-games',
  templateUrl: './add-multiple-games.component.html',
  styleUrls: ['./add-multiple-games.component.css']
})
export class AddMultipleGamesComponent implements OnInit {
  /**
   * The dropped/selected file
   */
  public file: File;

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

  /**
   * An array containing all database columns, the user can assign
   */
  public allDatabaseHeader: Array<DatabaseColumn<Game>> = [
    {
      title: 'Barcode',
      required: true,
      multiple: false,
      convert(value: string, entity: Game) {
        entity.barcode = value;
      }
    },
    {
      title: 'Titel',
      required: true,
      multiple: false,
      convert(value: string, entity: Game) {
        entity.title = value;
      }
    },
    {
      title: 'Autor',
      required: false,
      multiple: false,
      convert(value: string, entity: Game) {
        entity.author = value;
      }
    },
    {
      title: 'Verlag',
      required: false,
      multiple: false,
      convert(value: string, entity: Game) {
        entity.publisher = value;
      }
    },
    {
      title: 'Unbenutzt',
      required: false,
      multiple: true,
      convert(value: string, entity: Game) {
        // do nothing
      }
    }];

  constructor() {
  }

  ngOnInit() {
  }

  /**
   * Callback for the file-selection step
   *
   * @param {File} file The selected file
   */
  loadWorkbook(file: File): void {
    this.file = file;
  }

  /**
   * Callback for the sheet-selection step
   *
   * @param {Array<Array<string>>} data The data contained in the selected sheet
   */
  loadSheet(data: Array<Array<string>>): void {
    this.data = data;
  }

  /**
   * Callback for the check-sheet-content step
   *
   * @param table The content of the sheet split in the header row and the main content
   */
  loadTable(table: { header: Array<string>, data: Array<Array<string>> }): void {
    this.fileHeader = table.header;
    this.fileContent = table.data;
  }

  /**
   * Callback for the column-assignment step
   *
   * @param {Array<Game>} games The created game objects
   */
  loadAssignments(games: Array<Game>): void {
    console.log(games);
  }
}
