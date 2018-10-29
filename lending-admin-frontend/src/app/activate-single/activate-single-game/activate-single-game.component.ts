import {Component, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {Game} from '../../interfaces/server/game.interface';
import {GameService} from '../../core/game.service';
import {EntityService} from '../../core/entity.service';

@Component({
  selector: 'lending-activate-single-game',
  templateUrl: './activate-single-game.component.html',
  styleUrls: ['./activate-single-game.component.css']
})
export class ActivateSingleGameComponent implements OnInit {
  /**
   * The entered barcode
   */
  public barcode: string;

  /**
   * The game belonging to the entered barcode
   */
  public game: Game;

  constructor(public entityService: EntityService, public gameService: GameService) {
  }

  public ngOnInit() {
  }

  public onSubmit(form: FormGroup): void {
    // the content inside the barcode input field is valid
    if (form.valid) {
      this.entityService.activateBarcodes([this.barcode], response => {
        if (response.correctBarcodes && response.correctBarcodes.includes(this.barcode)) {
          this.gameService.selectGamesToBarcodes([this.barcode], games => {
            this.game = games[0];
            this.barcode = null;
            form.reset();
          });
        }
      });
    }
  }
}
