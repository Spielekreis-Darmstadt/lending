import {Component, forwardRef, OnInit} from '@angular/core';
import {MultipleAdditionModel} from '../multiple-addition.model';
import {MultipleIdentityCardAdditionModelService} from './multiple-identity-card-addition-model.service';

@Component({
  selector: 'lending-add-multiple-identity-cards',
  templateUrl: './add-multiple-identity-cards.component.html',
  styleUrls: ['./add-multiple-identity-cards.component.css'],
  providers: [{provide: MultipleAdditionModel, useExisting: forwardRef(() => MultipleIdentityCardAdditionModelService)}]
})
export class AddMultipleIdentityCardsComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
