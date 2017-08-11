import { Component, OnInit } from '@angular/core';
import {Game} from "../interfaces/game.interface";
import {GameService} from "../services/game.service";

function prepareRange(range: {min: number, max: number}): string {
  if (range) {
    if (range.min == range.max) {
      return range.min.toString();
    } else {
      return range.min + " - " + range.max;
    }
  } else {
    return "";
  }
};

@Component({
  selector: 'lending-show-all-games',
  templateUrl: './show-all-games.component.html',
  styleUrls: ['./show-all-games.component.css']
})
export class ShowAllGamesComponent implements OnInit {

  public data: Array<Game>;

  public settings = {
    actions: false,
    columns: {
      barcode: {
        title: 'Barcode'
      },
      title: {
        title: 'Titel'
      },
      author: {
        title: 'Autor'
      },
      publisher: {
        title: 'Verlag'
      },
      minAge: {
        title: 'Mindestalter'
      },
      playerCount: {
        title: 'Spielerzahl',
        valuePrepareFunction: (playerCount) => prepareRange(playerCount),
        filterFunction: (cell, search) => prepareRange(cell).includes(search)
      },
      duration: {
        title: 'Spieldauer',
        valuePrepareFunction: (duration) => prepareRange(duration),
        filterFunction: (cell, search) => prepareRange(cell).includes(search)
      },
      activated: {
        title: 'Aktiviert',
        valuePrepareFunction: (activated) => {
          if (activated) {
            return "Ja";
          } else {
            return "Nein"
          }
        },
        filter: {
          type: 'list',
          config: {
            selectText: 'Select...',
            list: [
              { value: true, title: "Ja" },
              { value: false, title: "Nein" }
            ]
          }
        }
      }
    }
  };

  constructor(private gameService: GameService) {
    gameService.selectGames(games => this.data = games);
  }

  ngOnInit() {
  }

}
