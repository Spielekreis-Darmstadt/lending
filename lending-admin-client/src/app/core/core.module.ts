import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {EntityService} from './entity.service';
import {GameService} from './game.service';
import {HttpClientModule} from '@angular/common/http';
import {SnotifyService, ToastDefaults} from 'ng-snotify';
import {IdentityCardService} from './identity-card.service';
import {EnvelopeService} from './envelope.service';
import {LendGameService} from './lend-game.service';
import {LendIdentityCardService} from './lend-identity-card.service';

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
    EnvelopeService,
    LendGameService,
    LendIdentityCardService
  ]
})
export class CoreModule {
}
