import { Component, OnInit } from '@angular/core';
import { SnotifyService } from 'ng-snotify';
import { MultipleActivationModel } from '../multiple-activation.model';
import { HotTableRegisterer } from '@handsontable/angular';

@Component({
  selector: 'activation-confirmation-step',
  templateUrl: './confirmation-step.component.html',
  styleUrls: ['./confirmation-step.component.scss']
})
export class ConfirmationStepComponent implements OnInit {
  constructor(private hotRegisterer: HotTableRegisterer, private snotifyService: SnotifyService, public model: MultipleActivationModel) {
  }

  ngOnInit() {
  }

  public beforeCellChange(changes: Array<any>): void {
    this.model.verified = false;
  }

  public validate(): void {
    const hot: any = this.hotRegisterer.getInstance('confirmation-hot-table');

    hot.validateCells(inputValid => {
      // clear all currently shown notifications
      this.snotifyService.clear();

      this.model.verified = inputValid;

      if (!inputValid) {
        this.snotifyService.warning('Mindestens ein Eintrag in der Tabelle ist nicht valide', { timeout: 0 });

        hot.render();
      }
    });
  }

  public confirmItems(): void {
    if (this.model.verified) {
      // try to activate the items into the database
      this.model.activateItems(this.model.items);
    }
  }
}
