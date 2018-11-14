import {Component, OnInit} from '@angular/core';
import {OverviewService} from '../overview.service';

@Component({
  selector: 'lending-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css']
})
export class OverviewComponent implements OnInit {

  constructor(public overviewService: OverviewService) {
  }

  ngOnInit() {
  }
  
}
