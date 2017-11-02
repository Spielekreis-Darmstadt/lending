import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {OverviewComponent} from './overview/overview.component';
import {AddSingleGameComponent} from './add-single/add-singe-game/add-single-game.component';
import {ShowAllGamesComponent} from './show-all/show-all-games/show-all-games.component';
import {AddMultipleGamesComponent} from './add-multiple/add-multiple-games/add-multiple-games.component';
import {ActivateMultipleGamesComponent} from './activate-multiple/activate-multiple-games/activate-multiple-games.component';
import {ActivateSingleGameComponent} from './activate-single/activate-single-game/activate-single-game.component';

const routes: Routes = [
  { path: 'games/add-single', component: AddSingleGameComponent, data: { title: 'Füge ein einzelnes Spiel hinzu' } },
  { path: 'games/add-multiple', component: AddMultipleGamesComponent, data: { title: 'Füge eine Tabelle mit Spielen hinzu' } },
  { path: 'games/activate-single', component: ActivateSingleGameComponent, data: { title: 'Aktiviere ein einzelnes Spiel' }},
  { path: 'games/activate-multiple', component: ActivateMultipleGamesComponent, data: { title: 'Aktiviere eine Tabelle mit Spielen' }},
  { path: 'games/show', component: ShowAllGamesComponent, data: { title: 'Zeige alle Spiele an' }},
  { path: '', component: OverviewComponent, data: { title: 'Überblick' } }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
