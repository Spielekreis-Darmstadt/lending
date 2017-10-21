import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {OverviewComponent} from './overview/overview.component';
import {AddSingeGameComponent} from './add-singe-game/add-singe-game.component';
import {BarcodeValidatorDirective} from './validators/barcode-validator.directive';
import {FormsModule} from '@angular/forms';
import {GameService} from './services/game.service';
import {HttpClientModule} from '@angular/common/http';
import {BarcodeService} from './services/barcode.service';
import {ShowAllGamesComponent} from './show-all-games/show-all-games.component';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import {DropZoneDirective} from './drop-zone.directive';
import {AddMultipleGamesComponent} from './add-multiple-games/add-multiple-games.component';
import {ArchwizardModule} from 'ng2-archwizard';
import {DndModule} from 'ng2-dnd';
import {FileSelectionStepComponent} from './file-selection-step/file-selection-step.component';
import {SheetSelectionStepComponent} from './sheet-selection-step/sheet-selection-step.component';
import {CheckSheetContentStepComponent} from './check-sheet-content-step/check-sheet-content-step.component';
import {ColumnAssignmentStepComponent} from './column-assignment-step/column-assignment-step.component';
import {HotTableModule} from 'angular-handsontable';
import { ConfirmationStepComponent } from './confirmation-step/confirmation-step.component';
import {SnotifyModule, SnotifyService, ToastDefaults} from 'ng-snotify';
import { SummaryStepComponent } from './summary-step/summary-step.component';
import {MultipleGameAdditionModelService} from './multiple-game-addition-model.service';

@NgModule({
  declarations: [
    AppComponent,
    OverviewComponent,
    AddSingeGameComponent,
    BarcodeValidatorDirective,
    ShowAllGamesComponent,
    DropZoneDirective,
    AddMultipleGamesComponent,
    FileSelectionStepComponent,
    SheetSelectionStepComponent,
    CheckSheetContentStepComponent,
    ColumnAssignmentStepComponent,
    ConfirmationStepComponent,
    SummaryStepComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    NgbModule.forRoot(),
    Ng2SmartTableModule,
    ArchwizardModule,
    DndModule.forRoot(),
    HotTableModule,
    SnotifyModule
  ],
  providers: [GameService, BarcodeService, MultipleGameAdditionModelService,
    { provide: 'SnotifyToastConfig', useValue: ToastDefaults}, SnotifyService],
  bootstrap: [AppComponent]
})
export class AppModule { }
