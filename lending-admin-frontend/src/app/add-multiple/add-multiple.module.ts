import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CheckSheetContentStepComponent } from './check-sheet-content-step/check-sheet-content-step.component';
import { ColumnAssignmentStepComponent } from './column-assignment-step/column-assignment-step.component';
import { ConfirmationStepComponent } from './confirmation-step/confirmation-step.component';
import { FileSelectionStepComponent } from './file-selection-step/file-selection-step.component';
import { SheetSelectionStepComponent } from './sheet-selection-step/sheet-selection-step.component';
import { AddMultipleGamesComponent } from './add-multiple-games/add-multiple-games.component';
import { MultipleGameAdditionModelService } from './add-multiple-games/multiple-game-addition-model.service';
import { ArchwizardModule } from 'angular-archwizard';
import { SnotifyModule } from 'ng-snotify';
import { FormsModule } from '@angular/forms';
import { SummaryStepComponent } from './summary-step/summary-step.component';
import { SharedModule } from '../shared/shared.module';
import { AddMultipleIdentityCardsComponent } from './add-multiple-identity-cards/add-multiple-identity-cards.component';
import { AddMultipleEnvelopesComponent } from './add-multiple-envelopes/add-multiple-envelopes.component';
import { MultipleIdentityCardAdditionModelService } from './add-multiple-identity-cards/multiple-identity-card-addition-model.service';
import { MultipleEnvelopeAdditionModelService } from './add-multiple-envelopes/multiple-envelope-addition-model.service';
import { HotTableModule } from '@handsontable/angular';
import { MatSelectModule, MatSlideToggleModule } from '@angular/material';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ArchwizardModule,
    MatSelectModule,
    MatSlideToggleModule,
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
    AddMultipleGamesComponent,
    AddMultipleIdentityCardsComponent,
    AddMultipleEnvelopesComponent
  ],
  exports: [
    AddMultipleGamesComponent,
    AddMultipleIdentityCardsComponent,
    AddMultipleEnvelopesComponent
  ],
  providers: [
    MultipleGameAdditionModelService,
    MultipleIdentityCardAdditionModelService,
    MultipleEnvelopeAdditionModelService
  ]
})
export class AddMultipleModule {
}
