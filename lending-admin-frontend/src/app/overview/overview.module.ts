import {NgModule} from '@angular/core';
import {CommonModule, registerLocaleData} from '@angular/common';
import {OverviewService} from './overview.service';
import {SharedModule} from '../shared/shared.module';
import {OverviewComponent} from './overview/overview.component';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import localeDe from '@angular/common/locales/de';

registerLocaleData(localeDe, 'de');

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    NgxChartsModule,
    BrowserAnimationsModule,
    MatCardModule
  ],
  declarations: [
    OverviewComponent
  ],
  providers: [
    OverviewService
  ],
  exports: [
    OverviewComponent
  ]
})
export class OverviewModule {
}
