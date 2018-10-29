import {Component} from '@angular/core';
import {ActivatedRouteSnapshot, NavigationEnd, Router} from '@angular/router';
import {Title} from '@angular/platform-browser';
import 'rxjs/add/operator/filter';

@Component({
  selector: 'lending-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  collapsed = true;

  constructor(private router: Router, private titleService: Title) {
    // for more details see https://stackoverflow.com/questions/34602806/how-to-change-page-title-in-angular2-router
    this.router.events
      .filter(event => event instanceof NavigationEnd)
      .subscribe(event => titleService.setTitle(this.getDeepestTitle(this.router.routerState.snapshot.root)));
  }

  private getDeepestTitle(routeSnapshot: ActivatedRouteSnapshot) {
    let title = routeSnapshot.data ? routeSnapshot.data['title'] : '';

    if (routeSnapshot.firstChild) {
      title = this.getDeepestTitle(routeSnapshot.firstChild) || title;
    }

    return title;
  }
}
