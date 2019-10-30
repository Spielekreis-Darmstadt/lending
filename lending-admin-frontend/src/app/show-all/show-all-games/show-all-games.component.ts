import {Component, OnInit, ViewChild} from '@angular/core';
import {Game} from "../../interfaces/server/game.interface";
import {GameService} from "../../core/game.service";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import {ChangeActivationModalComponent} from "../change-activation-modal/change-activation-modal.component";
import {SearchService} from "../../search.service";

@Component({
  selector: 'lending-show-all-games',
  templateUrl: './show-all-games.component.html',
  styleUrls: ['./show-all-games.component.css']
})
export class ShowAllGamesComponent implements OnInit {
  public displayedColumns: string[] = ['barcode', 'title', 'author', 'publisher', 'minAge', 'playerCount', 'duration', 'activated'];

  public dataSource = new MatTableDataSource<Game>();

  @ViewChild(MatSort, { static: true })
  public sort: MatSort;

  @ViewChild(MatPaginator, { static: true })
  public paginator: MatPaginator;

  constructor(private dialog: MatDialog, private gameService: GameService, private searchService: SearchService) {
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
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.dataSource.filter = this.searchService.searchTerm;

    this.searchService.searchTermSubject
      .subscribe(searchTerm => this.dataSource.filter = searchTerm);
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
