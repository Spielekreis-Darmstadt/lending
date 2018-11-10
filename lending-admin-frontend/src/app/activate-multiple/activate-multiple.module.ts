import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CheckSheetContentStepComponent} from './check-sheet-content-step/check-sheet-content-step.component';
import {ColumnAssignmentStepComponent} from './column-assignment-step/column-assignment-step.component';
import {ConfirmationStepComponent} from './confirmation-step/confirmation-step.component';
import {FileSelectionStepComponent} from './file-selection-step/file-selection-step.component';
import {SheetSelectionStepComponent} from './sheet-selection-step/sheet-selection-step.component';
import {ActivateMultipleGamesComponent} from './activate-multiple-games/activate-multiple-games.component';
import {MultipleGameActivationModelService} from './activate-multiple-games/multiple-game-activation-model.service';
import {ArchwizardModule} from 'angular-archwizard';
import {SnotifyModule} from 'ng-snotify';
import {FormsModule} from '@angular/forms';
import {SummaryStepComponent} from './summary-step/summary-step.component';
import {SharedModule} from '../shared/shared.module';
import {ActivateMultipleEnvelopesComponent} from './activate-multiple-envelopes/activate-multiple-envelopes.component';
import {ActivateMultipleIdentityCardsComponent} from './activate-multiple-identity-cards/activate-multiple-identity-cards.component';
import {MultipleEnvelopeActivationModelService} from './activate-multiple-envelopes/multiple-envelope-activation-model.service';
import {MultipleIdentityCardActivationModelService} from './activate-multiple-identity-cards/multiple-identity-card-activation-model.service';
import {HotTableModule} from '@handsontable/angular';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ArchwizardModule,
    HotTableModule.forRoot(),
    SnotifyModule,
    SharedModule
  ],
  declarations: [
    CheckSheetContentStepComponent,
    ColumnAssignmentStepComponent,
    ConfirmationStepComponent,
    FileSelectionStepComponent,
    SheetSelectionStepComponent,
    SummaryStepComponent,
    ActivateMultipleGamesComponent,
    ActivateMultipleEnvelopesComponent,
    ActivateMultipleIdentityCardsComponent
  ],
  exports: [
    ActivateMultipleGamesComponent,
    ActivateMultipleEnvelopesComponent,
    ActivateMultipleIdentityCardsComponent
  ],
  providers: [
    MultipleGameActivationModelService,
    MultipleEnvelopeActivationModelService,
    MultipleIdentityCardActivationModelService
  ]
})
export class ActivateMultipleModule {
}
