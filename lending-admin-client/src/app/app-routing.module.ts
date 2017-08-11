import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {OverviewComponent} from "./overview/overview.component";
import {AddSingeGameComponent} from "./add-singe-game/add-singe-game.component";
import {ShowAllGamesComponent} from "./show-all-games/show-all-games.component";

const routes: Routes = [
  { path: 'games/add-single', component: AddSingeGameComponent},
  { path: 'games/show', component: ShowAllGamesComponent},
  { path: '', component: OverviewComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
