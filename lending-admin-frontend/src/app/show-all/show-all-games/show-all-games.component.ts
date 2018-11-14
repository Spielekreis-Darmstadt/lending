import {Component, OnInit, ViewChild} from '@angular/core';
import {Game} from "../../interfaces/server/game.interface";
import {GameService} from "../../core/game.service";
import {MatDialog, MatPaginator, MatTableDataSource} from "@angular/material";
import {ChangeActivationModalComponent} from "../change-activation-modal/change-activation-modal.component";

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

  constructor(private dialog: MatDialog, private gameService: GameService) {
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

  /**
   * Selects the given game.
   * This opens a modal where the user can activate/deactivate the selected game
   *
   * @param game The selected game
   */
  selectGame(game: Game): void {
    this.dialog.open(ChangeActivationModalComponent, {
      data: {
        entityName: "Spiel",
        entity: game
      }
    });
  }
}
