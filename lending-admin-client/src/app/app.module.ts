import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {OverviewComponent} from './overview/overview.component';
import {FormsModule} from '@angular/forms';
import {ArchwizardModule} from 'ng2-archwizard';
import {AddMultipleModule} from './add-multiple/add-multiple.module';
import {CoreModule} from './core/core.module';
import {ActivateMultipleModule} from './activate-multiple/activate-multiple.module';
import {AddSingleModule} from './add-single/add-single.module';
import {ShowAllModule} from './show-all/show-all.module';
import {ActivateSingleModule} from './activate-single/activate-single.module';
import {ChartsModule} from 'ng2-charts';

@NgModule({
  declarations: [
    AppComponent,
    OverviewComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    NgbModule.forRoot(),
    CoreModule,
    AppRoutingModule,
    ShowAllModule,
    AddSingleModule,
    AddMultipleModule,
    ActivateSingleModule,
    ActivateMultipleModule,
    ChartsModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
