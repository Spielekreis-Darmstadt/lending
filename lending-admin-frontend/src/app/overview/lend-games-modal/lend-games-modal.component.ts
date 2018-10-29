import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {LendGameService} from '../../core/lend-game.service';
import {LendIdentityCardGroup} from '../../interfaces/server/lend-identity-card-group.interface';

@Component({
  selector: 'lending-lend-games-modal',
  templateUrl: './lend-games-modal.component.html',
  styleUrls: ['./lend-games-modal.component.css']
})
export class LendGamesModalComponent implements OnInit {
  public lendIdentityCardGroups: Array<LendIdentityCardGroup> = [];

  constructor(public activeModal: NgbActiveModal, private lendGameService: LendGameService) {
    this.lendGameService.selectAllLendGames(lendIdentityCardGroups => this.lendIdentityCardGroups = lendIdentityCardGroups);
  }

  ngOnInit() {
  }

}
