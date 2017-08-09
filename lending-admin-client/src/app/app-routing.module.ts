import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {OverviewComponent} from "./overview/overview.component";
import {AddSingeGameComponent} from "./add-singe-game/add-singe-game.component";

const routes: Routes = [
  { path: 'games/add-single', component: AddSingeGameComponent},
  { path: '', component: OverviewComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
