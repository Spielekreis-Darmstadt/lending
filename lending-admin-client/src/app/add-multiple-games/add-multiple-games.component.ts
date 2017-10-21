import {Component, forwardRef, OnInit} from '@angular/core';
import {MultipleAdditionModel} from '../multiple-addition.model';
import {MultipleGameAdditionModelService} from '../multiple-game-addition-model.service';

/**
 * A component used to add a list of games at once.
 * This list needs to be provided as a table file, e.g. as an xls or csv file
 */
@Component({
  selector: 'lending-add-multiple-games',
  templateUrl: './add-multiple-games.component.html',
  styleUrls: ['./add-multiple-games.component.css'],
  providers: [ { provide: MultipleAdditionModel, useExisting: forwardRef(() => MultipleGameAdditionModelService) } ]
})
export class AddMultipleGamesComponent implements OnInit {
    constructor() {
  }

  ngOnInit() {
  }
}
