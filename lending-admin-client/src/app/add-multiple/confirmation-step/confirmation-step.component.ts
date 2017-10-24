import {Component, OnInit} from '@angular/core';
import {HotRegisterer} from 'angular-handsontable';
import {SnotifyService} from 'ng-snotify';
import Handsontable from 'handsontable';
import {MultipleAdditionModel} from '../multiple-addition.model';
import {Lendable} from '../../interfaces/server/lendable.interface';

@Component({
  selector: 'lending-confirmation-step',
  templateUrl: './confirmation-step.component.html',
  styleUrls: ['./confirmation-step.component.css']
})
export class ConfirmationStepComponent implements OnInit {
  constructor(private hotRegisterer: HotRegisterer, private snotifyService: SnotifyService, public model: MultipleAdditionModel<Lendable>) {
  }

  ngOnInit() {
    this.model.columns.forEach(column => {
      column.renderer = (hotInstance, td, row, col, prop, value, cellProperties) => {
        // depending on the base type apply the correct base renderer
        switch (column.type) {
          case 'numeric':
            Handsontable.renderers.NumericRenderer.apply(this, [hotInstance, td, row, col, prop, value, cellProperties]);
            break;
          case 'text':
            Handsontable.renderers.TextRenderer.apply(this, [hotInstance, td, row, col, prop, value, cellProperties]);
            break;
          default:
        }

        // if the barcode for the entry is one of the "bad" barcode color the cell yellow
        if (this.model.verificationResult.badBarcodes && this.model.verificationResult.badBarcodes.includes(this.model.items[row].barcode)) {
          td.style.background = 'yellow';
        }
      };
    });
  }

  public beforeCellChange(changes: Array<any>): void {
    this.model.verificationResult.verified = false;
  };

  public validate(): void {
    const hot: any = this.hotRegisterer.getInstance('confirmation-hot-table');

    hot.validateCells(inputValid => {
      // clear all currently shown notifications
      this.snotifyService.clear();

      if (inputValid) {
        this.model.verifyItems(this.model.items);
      } else {
        this.model.verificationResult = {verified: false};
        this.snotifyService.warning('Mindestens ein Eintrag in der Tabelle ist nicht valide', {timeout: 0});

        hot.render();
      }
    });
  }

  public confirmItems(): void {
    if (this.model.verificationResult.verified) {
      // set the correct activated state
      this.model.items.forEach(item => item.activated = this.model.activateItems);

      // try to insert the items into the database
      this.model.insertItems(this.model.items);
    }
  }
}
