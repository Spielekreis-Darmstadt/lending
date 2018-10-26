import {Component, forwardRef, OnInit} from '@angular/core';
import {MultipleEnvelopeAdditionModelService} from './multiple-envelope-addition-model.service';
import {MultipleAdditionModel} from '../multiple-addition.model';

@Component({
  selector: 'lending-add-multiple-envelopes',
  templateUrl: './add-multiple-envelopes.component.html',
  styleUrls: ['./add-multiple-envelopes.component.css'],
  providers: [{provide: MultipleAdditionModel, useExisting: forwardRef(() => MultipleEnvelopeAdditionModelService)}]
})
export class AddMultipleEnvelopesComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
