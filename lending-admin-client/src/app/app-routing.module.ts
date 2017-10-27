import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {OverviewComponent} from './overview/overview.component';
import {AddSingleGameComponent} from './add-single/add-singe-game/add-single-game.component';
import {ShowAllGamesComponent} from './show-all/show-all-games/show-all-games.component';
import {AddMultipleGamesComponent} from './add-multiple/add-multiple-games/add-multiple-games.component';
import {ActivateMultipleGamesComponent} from './activate-multiple/activate-multiple-games/activate-multiple-games.component';
import {ActivateSingleGameComponent} from './activate-single/activate-single-game/activate-single-game.component';

const routes: Routes = [
  { path: 'games/add-single', component: AddSingleGameComponent},
  { path: 'games/add-multiple', component: AddMultipleGamesComponent},
  { path: 'games/activate-single', component: ActivateSingleGameComponent},
  { path: 'games/activate-multiple', component: ActivateMultipleGamesComponent},
  { path: 'games/show', component: ShowAllGamesComponent},
  { path: '', component: OverviewComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
