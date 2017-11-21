import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {OverviewService} from './overview.service';
import {SharedModule} from '../shared/shared.module';
import {ChartsModule} from 'ng2-charts';
import {OverviewComponent} from './overview/overview.component';
import {LendGamesModalComponent} from './lend-games-modal/lend-games-modal.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    ChartsModule
  ],
  declarations: [
    OverviewComponent,
    LendGamesModalComponent
  ],
  providers: [
    OverviewService
  ],
  exports: [
    OverviewComponent
  ],
  entryComponents: [
    LendGamesModalComponent
  ]
})
export class OverviewModule {
}
