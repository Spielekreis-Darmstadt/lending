import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {EntityService} from './entity.service';
import {GameService} from './game.service';
import {HttpClientModule} from '@angular/common/http';
import {SnotifyService, ToastDefaults} from 'ng-snotify';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule
  ],
  providers: [
    {provide: 'SnotifyToastConfig', useValue: ToastDefaults},
    SnotifyService,
    EntityService,
    GameService
  ]
})
export class CoreModule {
}
