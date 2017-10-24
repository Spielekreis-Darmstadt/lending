import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {OverviewComponent} from './overview/overview.component';
import {AddSingeGameComponent} from './add-singe-game/add-singe-game.component';
import {FormsModule} from '@angular/forms';
import {GameService} from './core/game.service';
import {HttpClientModule} from '@angular/common/http';
import {BarcodeService} from './core/barcode.service';
import {ShowAllGamesComponent} from './show-all-games/show-all-games.component';
import {Ng2SmartTableModule} from 'ng2-smart-table';
import {ArchwizardModule} from 'ng2-archwizard';
import {SnotifyModule, SnotifyService, ToastDefaults} from 'ng-snotify';
import {AddMultipleModule} from './add-multiple/add-multiple.module';
import {CoreModule} from './core/core.module';
import {SharedModule} from './shared/shared.module';

@NgModule({
  declarations: [
    AppComponent,
    OverviewComponent,
    AddSingeGameComponent,
    ShowAllGamesComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    CoreModule,
    SharedModule,
    HttpClientModule,
    AppRoutingModule,
    NgbModule.forRoot(),
    Ng2SmartTableModule,
    SnotifyModule,
    AddMultipleModule
  ],
  providers: [
    {provide: 'SnotifyToastConfig', useValue: ToastDefaults},
    SnotifyService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
