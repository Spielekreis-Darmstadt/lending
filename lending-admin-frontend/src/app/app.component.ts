import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRouteSnapshot, NavigationEnd, Router} from '@angular/router';
import {Title} from '@angular/platform-browser';
import {filter} from 'rxjs/operators';
import {SearchService} from "./search.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'lending-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  public collapsed = true;

  @ViewChild('searchForm')
  public searchForm: NgForm;

  constructor(private router: Router, private titleService: Title, private searchService: SearchService) {
  }

  ngOnInit(): void {
    // for more details see https://stackoverflow.com/questions/34602806/how-to-change-page-title-in-angular2-router
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(event => {
        // change the title of the whole browser tab
        this.titleService.setTitle(this.getDeepestTitle(this.router.routerState.snapshot.root));
        // clear the search field
        this.clearSearchTerm();
      });
  }

  private getDeepestTitle(routeSnapshot: ActivatedRouteSnapshot) {
    let title = routeSnapshot.data ? routeSnapshot.data['title'] : '';

    if (routeSnapshot.firstChild) {
      title = this.getDeepestTitle(routeSnapshot.firstChild) || title;
    }

    return title;
  }

  public processSearchTerm(): void {
    this.searchService.searchTerm = this.searchForm.value.searchTerm;
  }

  public clearSearchTerm(): void {
    this.searchForm.reset();
    this.searchService.searchTerm = null;
  }
}
