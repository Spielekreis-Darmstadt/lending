import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {OverviewService} from './overview.service';
import {SharedModule} from '../shared/shared.module';
import {OverviewComponent} from './overview/overview.component';
import {LendGamesModalComponent} from './lend-games-modal/lend-games-modal.component';
import {LendIdentityCardsModalComponent} from './lend-identity-cards-modal/lend-identity-cards-modal.component';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatCardModule} from '@angular/material';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    NgxChartsModule,
    BrowserAnimationsModule,
    MatCardModule
  ],
  declarations: [
    OverviewComponent,
    LendGamesModalComponent,
    LendIdentityCardsModalComponent
  ],
  providers: [
    OverviewService
  ],
  exports: [
    OverviewComponent
  ],
  entryComponents: [
    LendGamesModalComponent,
    LendIdentityCardsModalComponent
  ]
})
export class OverviewModule {
}
