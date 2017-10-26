import {Component, forwardRef, OnInit} from '@angular/core';
import {MultipleActivationModel} from '../multiple-activation.model';
import {MultipleGameActivationModelService} from './multiple-game-activation-model.service';

/**
 * A component used to activate a list of games, represented by their barcodes, at once.
 * This list needs to be provided as a table file, e.g. as an xls or csv file
 */
@Component({
  selector: 'lending-activate-multiple-games',
  templateUrl: './activate-multiple-games.component.html',
  styleUrls: ['./activate-multiple-games.component.css'],
  providers: [ { provide: MultipleActivationModel, useExisting: forwardRef(() => MultipleGameActivationModelService) } ]
})
export class ActivateMultipleGamesComponent implements OnInit {
    constructor() {
  }

  ngOnInit() {
  }
}
