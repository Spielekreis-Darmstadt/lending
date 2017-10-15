import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {HotRegisterer} from 'angular-handsontable';
import {VerificationResult} from '../interfaces/verification-result.interface';
import {SnotifyService} from 'ng-snotify';
import Handsontable from 'handsontable';

@Component({
  selector: 'lending-confirmation-step',
  templateUrl: './confirmation-step.component.html',
  styleUrls: ['./confirmation-step.component.css']
})
export class ConfirmationStepComponent implements OnInit {
  /**
   * An array containing all items to be confirmed
   */
  @Input()
  public items: Array<any>;

  /**
   * A validator used to query the server if the given items are valid and can be added to the database
   */
  @Input()
  public itemValidator: (items: Array<any>, callback: (verificationResult: VerificationResult) => void) => void;

  /**
   * An array containing the column descriptions for the handsontable instance showing the `items` array
   */
  @Input()
  public columns: Array<any>;

  /**
   * An array containing the header labels for the handsontable instance
   */
  @Input()
  public columnHeaders: Array<string>;

  /**
   * An event emitter, which gets called when the items are confirmed.
   * This event emitter is then called with the confirmed items
   *
   * @type {EventEmitter<any>}
   */
  @Output()
  public onItemsConfirmed: EventEmitter<Array<any>> = new EventEmitter();

  /**
   * The server result of the verification of the items inside the handsontable instance
   *
   * @type {{verified: boolean}}
   */
  public verificationResult: VerificationResult = { verified: false };

  private textServerVerificationRenderer = (hotInstance, td, row, col, prop, value, cellProperties) => {
    Handsontable.renderers.TextRenderer.apply(this, [hotInstance, td, row, col, prop, value, cellProperties]);

    if (this.verificationResult.badBarcodes && this.verificationResult.badBarcodes.includes(this.items[row].barcode)) {
      td.style.background = 'yellow';
    }
  };

  private numericServerVerificationRenderer = (hotInstance, td, row, col, prop, value, cellProperties) => {
    Handsontable.renderers.NumericRenderer.apply(this, [hotInstance, td, row, col, prop, value, cellProperties]);

    if (this.verificationResult.badBarcodes && this.verificationResult.badBarcodes.includes(this.items[row].barcode)) {
      td.style.background = 'yellow';
    }
  };

  constructor(private hotRegisterer: HotRegisterer, private snotifyService: SnotifyService) {
  }

  ngOnInit() {
    this.columns.forEach(column => {
      switch (column.type) {
        case 'numeric':
          column.renderer = this.numericServerVerificationRenderer;
          break;
        case 'text':
          column.renderer = this.textServerVerificationRenderer;
          break;
        default:
      }
    });
  }

  public beforeCellChange(changes: Array<any>): void {
    this.verificationResult.verified = false;
  };

  public validate(): void {
    const hot: any = this.hotRegisterer.getInstance('confirmation-hot-table');

    hot.validateCells(inputValid => {
      // clear all currently shown notifications
      this.snotifyService.clear();

      if (inputValid) {
        this.itemValidator(this.items, verificationResult => {
          this.verificationResult = verificationResult;

          if (!verificationResult.verified) {
            if (!verificationResult.badBarcodes || verificationResult.badBarcodes.length == 0) {
              this.snotifyService.error('Es ist ein unerwarteter Fehler aufgetreten', { timeout: 0 });
            } else {
              this.snotifyService.warning('Bei einigen Einträgen fehlt entweder der Titel oder der Barcode existiert bereits', { timeout: 0 });
            }
          } else {
            this.snotifyService.success('Alle Einträge sind valide', { timeout: 0 });
          }

          hot.render();
        });
      } else {
        this.verificationResult = { verified: false };
        this.snotifyService.warning('Mindestens ein Eintrag in der Tabelle ist nicht valide', { timeout: 0 });

        hot.render();
      }
    });
  }

  public confirmItems(): void {
    if (this.verificationResult.verified) {
      this.onItemsConfirmed.emit(this.items);
    }
  }
}
