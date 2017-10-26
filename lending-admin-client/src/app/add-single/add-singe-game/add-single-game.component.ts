import {Component, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {Game, GameInstance} from '../../interfaces/server/game.interface';
import {GameService} from '../../core/game.service';
import {AddGamesResponse} from '../../interfaces/server/add-games-response.interface';

/**
 * A component used to enter a single new game at a time
 *
 * @author Marc Arndt
 */
@Component({
  selector: 'lending-add-single-game',
  templateUrl: './add-single-game.component.html',
  styleUrls: ['./add-single-game.component.css']
})
export class AddSingleGameComponent implements OnInit {

  /**
   * Success information about the last inputted game
   */
  public success: boolean;
  public successMessage: string;

  /**
   * The model of the current game
   */
  public model: Game;

  /**
   * Constructor
   * @param {GameService} addGameService A service used to send a to be added game to the server
   */
  constructor(private addGameService: GameService) {
    this.model = new GameInstance();
  }

  ngOnInit() {
  }

  /**
   * Resets the given form
   * @param {FormGroup} form The form to be reset
   */
  reset(form: FormGroup): void {
    // set the wizard as unfinished/unsuccessful
    this.success = false;
    // reset the form
    form.reset();
  }

  /**
   * Submits the entered game information to the server as a new game
   * @param {FormGroup} form The form containing the inputted information
   */
  onSubmit(form: FormGroup): void {
    const newGame: Game = {barcode: form.value.barcode, title: form.value.title};

    if (form.value.author) {
      newGame.author = form.value.author;
    }

    if (form.value.publisher) {
      newGame.publisher = form.value.publisher;
    }

    if (form.value.minAge) {
      newGame.minAge = form.value.minAge;
    }

    if (form.value.comment) {
      newGame.comment = form.value.comment;
    }

    if (form.value.minPlayers && form.value.maxPlayers) {
      newGame.playerCount = {min: form.value.minPlayers, max: form.value.maxPlayers};
    }

    if (form.value.minDuration && form.value.maxDuration) {
      newGame.duration = {min: form.value.minDuration, max: form.value.maxDuration};
    }

    if (form.value.activated) {
      newGame.activated = form.value.activated;
    }

    this.addGameService.addGame(newGame,
      (result: AddGamesResponse) => {
        if (!result) {
          this.success = false;
          this.successMessage = 'Es ist ein Fehler beim Hinzufügen des Spiels aufgetreten';
        } else {
          // set the success flag
          this.success = result.success;

          if (result.success) {
            // reset the form
            form.reset();

            // show success message
            this.successMessage = `Das Spiel ${newGame.barcode} wurde erfolgreich hinzugefügt`;
          } else {
            // show correct error message
            if (result.emptyTitleBarcodes && result.emptyTitleBarcodes.length > 0) {
              this.successMessage = `Das Spiel ${newGame.barcode} besitzt keinen Titel`;
            }
            if (result.alreadyExistingBarcodes && result.alreadyExistingBarcodes.length > 0) {
              this.successMessage = `Ein Spiel mit dem Barcode ${newGame.barcode} existiert bereits`;
            }
          }
        }
      });
  }
}
