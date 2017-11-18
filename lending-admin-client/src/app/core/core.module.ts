import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {EntityService} from './entity.service';
import {GameService} from './game.service';
import {HttpClientModule} from '@angular/common/http';
import {SnotifyService, ToastDefaults} from 'ng-snotify';
import {IdentityCardService} from './identity-card.service';
import {EnvelopeService} from './envelope.service';
import {OverviewService} from '../overview/overview.service';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule
  ],
  providers: [
    {provide: 'SnotifyToastConfig', useValue: ToastDefaults},
    SnotifyService,
    EntityService,
    GameService,
    IdentityCardService,
    EnvelopeService
  ]
})
export class CoreModule {
}
