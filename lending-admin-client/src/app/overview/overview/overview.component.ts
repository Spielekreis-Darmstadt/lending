import { Component, OnInit } from '@angular/core';
import {OverviewService} from '../overview.service';

@Component({
  selector: 'lending-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css']
})
export class OverviewComponent implements OnInit {

  public barChartOptions = {
    scales : {
      yAxes: [{
        ticks: {
          min : 0
        }
      }]
    },
    legend: {
      display: false
    },
    tooltips: {
      callbacks: {
        label: function(tooltipItem) {
          return tooltipItem.yLabel;
        }
      }
    }
  };

  public gameChartLabels = ['Alle Spiele', 'Verliehene Spiele'];

  public identityCardChartLabels = ['Alle Ausweise', 'Vergebene Ausweise'];

  constructor(public overviewService: OverviewService) { }

  ngOnInit() {
  }

}
