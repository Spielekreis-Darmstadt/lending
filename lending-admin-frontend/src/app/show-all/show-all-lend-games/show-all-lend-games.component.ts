import {Component, OnInit} from '@angular/core';
import {LendGameService} from "../../core/lend-game.service";
import {LendIdentityCardGroup} from "../../interfaces/server/lend-identity-card-group.interface";

@Component({
  selector: 'lending-show-all-lend-games',
  templateUrl: './show-all-lend-games.component.html',
  styleUrls: ['./show-all-lend-games.component.css']
})
export class ShowAllLendGamesComponent implements OnInit {
  public lendIdentityCardGroups: Array<LendIdentityCardGroup> = [];

  constructor(private lendGameService: LendGameService) {
    this.lendGameService.selectAllLendGames(lendIdentityCardGroups => this.lendIdentityCardGroups = lendIdentityCardGroups);
  }

  ngOnInit() {
  }

}
