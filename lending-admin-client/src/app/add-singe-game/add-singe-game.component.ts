import {Component, OnInit} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {Game, GameInstance} from "../interfaces/game.interface";
import {AddGameService} from "../services/add-game.service";

/**
 * A component used to enter a single new game at a time
 *
 * @author Marc Arndt
 */
@Component({
  selector: 'lending-add-singe-game',
  templateUrl: './add-singe-game.component.html',
  styleUrls: ['./add-singe-game.component.css']
})
export class AddSingeGameComponent implements OnInit {

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
   * @param {AddGameService} addGameService A service used to send a to be added game to the server
   */
  constructor(private addGameService: AddGameService) {
    this.model = new GameInstance();
  }

  ngOnInit() {
  }

  /**
   * Resets the given form
   * @param {FormGroup} form The form to be reset
   */
  reset(form: FormGroup): void {
    this.success = false;
    form.reset();
  }

  /**
   * Submits the entered game information to the server as a new game
   * @param {FormGroup} form The form containing the inputted information
   */
  onSubmit(form: FormGroup): void {
    console.log("Submitted");

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

    console.log(newGame);

    this.addGameService.addGame(newGame,
      (result, message) => {
        if (result) {
          this.reset(form);
        }

        this.success = result;
        this.successMessage = message;
      });
  }
}
