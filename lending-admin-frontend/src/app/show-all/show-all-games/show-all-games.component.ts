import {Component, OnInit, ViewChild} from '@angular/core';
import {Game} from "../../interfaces/server/game.interface";
import {GameService} from "../../core/game.service";
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {Envelope} from "../../interfaces/server/envelope.interface";

@Component({
  selector: 'lending-show-all-games',
  templateUrl: './show-all-games.component.html',
  styleUrls: ['./show-all-games.component.css']
})
export class ShowAllGamesComponent implements OnInit {
  public displayedColumns: string[] = ['barcode', 'title', 'author', 'publisher', 'minAge', 'playerCount', 'duration', 'activated'];

  public dataSource = new MatTableDataSource<Game>();

  @ViewChild(MatPaginator)
  public paginator: MatPaginator;

  constructor(private gameService: GameService) {
    gameService.selectGames(games => this.dataSource.data = games);
  }

  public prepareRange(range: { min: number, max: number }): string {
    if (range) {
      if (range.min === range.max) {
        return range.min.toString();
      } else {
        return range.min + ' - ' + range.max;
      }
    } else {
      return '';
    }
  }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
  }
}
