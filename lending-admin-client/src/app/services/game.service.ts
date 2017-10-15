import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Game} from "../interfaces/game.interface";
import {AddGameResponse} from "../interfaces/add-game-response.interface";
import {VerifyGamesResponse} from '../interfaces/verify-games-response.interface';

/**
 * A service used to do game related interaction with the server
 *
 * @author Marc Arndt
 */
@Injectable()
export class GameService {

  /**
   * Constructor
   * @param {HttpClient} http A http client
   */
  constructor(private http: HttpClient) {
  }

  /**
   * Tries to add a given game `game` on the server.
   * Afterwards the given callback `resultCallback` is called.
   * If the game addition succeeded the callback is called with `true`, otherwise with `false`.
   * @param {Game} game The game to be added
   * @param {(boolean, string) => void} resultCallback The callback to be called afterwards
   */
  addGame(game: Game, resultCallback: ((success: boolean, responseMessage: string) => void)): void {
    this.http
      .put('/lending-admin-server/rest/games/add', game)
      .subscribe(
        (data: AddGameResponse) => resultCallback(data.success, data.responseMessage),
        (err: HttpErrorResponse) => resultCallback(false, undefined)
      );
  }

  selectGames(resultCallback: ((games: Array<Game>) => void)): void {
    this.http
      .get('/lending-admin-server/rest/games/all')
      .subscribe(
        (data: Array<Game>) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback([])
      );
  }

  verifyGames(games: Array<Game>, resultCallback: ((valid: VerifyGamesResponse) => void)): void {
    this.http
      .put('/lending-admin-server/rest/games/verify', games)
      .subscribe(
        (data: VerifyGamesResponse) => resultCallback(data),
        (err: HttpErrorResponse) => resultCallback(null)
      );
  }
}
