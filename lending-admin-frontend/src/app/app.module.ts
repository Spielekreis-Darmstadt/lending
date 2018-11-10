import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule} from '@angular/forms';
import {AddMultipleModule} from './add-multiple/add-multiple.module';
import {CoreModule} from './core/core.module';
import {ActivateMultipleModule} from './activate-multiple/activate-multiple.module';
import {AddSingleModule} from './add-single/add-single.module';
import {ShowAllModule} from './show-all/show-all.module';
import {ActivateSingleModule} from './activate-single/activate-single.module';
import {OverviewModule} from './overview/overview.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    NgbModule,
    CoreModule,
    AppRoutingModule,
    ShowAllModule,
    AddSingleModule,
    AddMultipleModule,
    ActivateSingleModule,
    ActivateMultipleModule,
    OverviewModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
