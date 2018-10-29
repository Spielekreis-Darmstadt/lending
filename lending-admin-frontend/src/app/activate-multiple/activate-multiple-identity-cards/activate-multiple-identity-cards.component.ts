import {Component, forwardRef} from '@angular/core';
import {MultipleActivationModel} from '../multiple-activation.model';
import {MultipleIdentityCardActivationModelService} from './multiple-identity-card-activation-model.service';

@Component({
  selector: 'lending-activate-multiple-identity-cards',
  templateUrl: './activate-multiple-identity-cards.component.html',
  styleUrls: ['./activate-multiple-identity-cards.component.css'],
  providers: [{provide: MultipleActivationModel, useExisting: forwardRef(() => MultipleIdentityCardActivationModelService)}]
})
export class ActivateMultipleIdentityCardsComponent {

  constructor() {
  }

}
