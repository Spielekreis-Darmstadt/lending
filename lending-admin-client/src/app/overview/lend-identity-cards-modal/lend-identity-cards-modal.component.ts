import { Component, OnInit } from '@angular/core';
import {LendIdentityCard} from '../../interfaces/server/lend-identity-card.interface';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {LendIdentityCardService} from '../../core/lend-identity-card.service';

@Component({
  selector: 'lending-lend-identity-cards-modal',
  templateUrl: './lend-identity-cards-modal.component.html',
  styleUrls: ['./lend-identity-cards-modal.component.css']
})
export class LendIdentityCardsModalComponent implements OnInit {

  public lendIdentityCards: Array<LendIdentityCard> = [];

  constructor(public activeModal: NgbActiveModal, private lendIdentityCardService: LendIdentityCardService) {
    this.lendIdentityCardService.selectAllLendIdentityCards(lendIdentityCards => this.lendIdentityCards = lendIdentityCards);
  }

  ngOnInit() {
  }

}
