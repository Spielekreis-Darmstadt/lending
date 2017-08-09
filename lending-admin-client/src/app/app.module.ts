import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {OverviewComponent} from './overview/overview.component';
import {AddSingeGameComponent} from './add-singe-game/add-singe-game.component';
import {BarcodeValidatorDirective} from './validators/barcode-validator.directive';
import {FormsModule} from "@angular/forms";
import {AddGameService} from "./services/add-game.service";
import {HttpClientModule} from "@angular/common/http";
import {BarcodeService} from "./services/barcode.service";

@NgModule({
  declarations: [
    AppComponent,
    OverviewComponent,
    AddSingeGameComponent,
    BarcodeValidatorDirective
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    NgbModule.forRoot()
  ],
  providers: [AddGameService, BarcodeService],
  bootstrap: [AppComponent]
})
export class AppModule { }
