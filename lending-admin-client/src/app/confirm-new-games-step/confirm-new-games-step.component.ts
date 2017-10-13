import {Component, Input, OnInit} from '@angular/core';
import {Game} from '../interfaces/game.interface';

@Component({
  selector: 'lending-confirm-new-games-step',
  templateUrl: './confirm-new-games-step.component.html',
  styleUrls: ['./confirm-new-games-step.component.css']
})
export class ConfirmNewGamesStepComponent implements OnInit {
  @Input()
  public games: Array<Game>;

  @Input()
  public columns: any[];

  @Input()
  public columnHeaders: Array<string>;

  constructor() { }

  ngOnInit() {
  }

}
