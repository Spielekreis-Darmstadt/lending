import {Component, forwardRef} from '@angular/core';
import {MultipleActivationModel} from '../multiple-activation.model';
import {MultipleEnvelopeActivationModelService} from './multiple-envelope-activation-model.service';

@Component({
  selector: 'lending-activate-multiple-envelopes',
  templateUrl: './activate-multiple-envelopes.component.html',
  styleUrls: ['./activate-multiple-envelopes.component.css'],
  providers: [{provide: MultipleActivationModel, useExisting: forwardRef(() => MultipleEnvelopeActivationModelService)}]
})
export class ActivateMultipleEnvelopesComponent {

  constructor() {
  }

}
