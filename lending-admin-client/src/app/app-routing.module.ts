import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {OverviewComponent} from './overview/overview.component';
import {AddSingleGameComponent} from './add-single/add-singe-game/add-single-game.component';
import {ShowAllGamesComponent} from './show-all/show-all-games/show-all-games.component';
import {AddMultipleGamesComponent} from './add-multiple/add-multiple-games/add-multiple-games.component';
import {ActivateMultipleGamesComponent} from './activate-multiple/activate-multiple-games/activate-multiple-games.component';
import {ActivateSingleGameComponent} from './activate-single/activate-single-game/activate-single-game.component';
import {AddMultipleEnvelopesComponent} from './add-multiple/add-multiple-envelopes/add-multiple-envelopes.component';
import {AddMultipleIdentityCardsComponent} from './add-multiple/add-multiple-identity-cards/add-multiple-identity-cards.component';
import {ShowAllEnvelopesComponent} from './show-all/show-all-envelopes/show-all-envelopes.component';
import {ShowAllIdentityCardsComponent} from './show-all/show-all-identity-cards/show-all-identity-cards.component';
import {ActivateMultipleIdentityCardsComponent} from './activate-multiple/activate-multiple-identity-cards/activate-multiple-identity-cards.component';
import {ActivateMultipleEnvelopesComponent} from './activate-multiple/activate-multiple-envelopes/activate-multiple-envelopes.component';

const routes: Routes = [
  { path: 'games/add-single', component: AddSingleGameComponent, data: { title: 'Füge ein einzelnes Spiel hinzu' } },
  { path: 'games/add-multiple', component: AddMultipleGamesComponent, data: { title: 'Füge eine Tabelle mit Spielen hinzu' } },
  { path: 'games/activate-single', component: ActivateSingleGameComponent, data: { title: 'Aktiviere ein einzelnes Spiel' }},
  { path: 'games/activate-multiple', component: ActivateMultipleGamesComponent, data: { title: 'Aktiviere eine Tabelle mit Spielen' }},
  { path: 'games/show', component: ShowAllGamesComponent, data: { title: 'Zeige alle Spiele an' }},
  { path: 'identity-cards/add-multiple', component: AddMultipleIdentityCardsComponent, data: { title: 'Füge eine Tabelle mit Ausweisen hinzu' } },
  { path: 'identity-cards/activate-multiple', component: ActivateMultipleIdentityCardsComponent, data: { title: 'Aktiviere eine Tabelle mit Ausweisen' }},
  { path: 'identity-cards/show', component: ShowAllIdentityCardsComponent, data: { title: 'Zeige alle Ausweise an' } },
  { path: 'envelopes/add-multiple', component: AddMultipleEnvelopesComponent, data: { title: 'Füge eine Tabelle mit Umschlägen hinzu' } },
  { path: 'envelopes/activate-multiple', component: ActivateMultipleEnvelopesComponent, data: { title: 'Aktiviere eine Tabelle mit Umschlägen' }},
  { path: 'envelopes/show', component: ShowAllEnvelopesComponent, data: { title: 'Zeige alle Umschläge an' } },
  { path: '', component: OverviewComponent, data: { title: 'Überblick' } }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
