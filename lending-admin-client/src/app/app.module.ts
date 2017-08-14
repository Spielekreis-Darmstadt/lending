import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {OverviewComponent} from './overview/overview.component';
import {AddSingeGameComponent} from './add-singe-game/add-singe-game.component';
import {BarcodeValidatorDirective} from './validators/barcode-validator.directive';
import {FormsModule} from "@angular/forms";
import {GameService} from "./services/game.service";
import {HttpClientModule} from "@angular/common/http";
import {BarcodeService} from "./services/barcode.service";
import { ShowAllGamesComponent } from './show-all-games/show-all-games.component';
import {Ng2SmartTableModule} from "ng2-smart-table";
import { DropZoneDirective } from './drop-zone.directive';
import { AddMultipleGamesComponent } from './add-multiple-games/add-multiple-games.component';
import {WizardModule} from "ng2-archwizard/dist";
import {DndModule} from 'ng2-dnd';
import { FileSelectionStepComponent } from './file-selection-step/file-selection-step.component';
import { SheetSelectionStepComponent } from './sheet-selection-step/sheet-selection-step.component';
import { CheckSheetContentStepComponent } from './check-sheet-content-step/check-sheet-content-step.component';
import { ColumnAssignmentStepComponent } from './column-assignment-step/column-assignment-step.component';

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
    ColumnAssignmentStepComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    NgbModule.forRoot(),
    Ng2SmartTableModule,
    WizardModule,
    DndModule.forRoot()
  ],
  providers: [GameService, BarcodeService],
  bootstrap: [AppComponent]
})
export class AppModule { }
