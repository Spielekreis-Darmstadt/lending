import {Injectable} from '@angular/core';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  public searchTermSubject: Subject<string> = new Subject();

  private _searchTerm: string;

  public get searchTerm(): string {
    return this._searchTerm;
  }

  public set searchTerm(searchTerm: string) {
    this._searchTerm = searchTerm;
    this.searchTermSubject.next(searchTerm);
  }

  constructor() {
  }
}
