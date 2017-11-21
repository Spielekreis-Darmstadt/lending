import {Component, OnInit} from '@angular/core';
import {OverviewService} from '../overview.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {LendGamesModalComponent} from '../lend-games-modal/lend-games-modal.component';
import {LendIdentityCardsModalComponent} from '../lend-identity-cards-modal/lend-identity-cards-modal.component';

@Component({
  selector: 'lending-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css']
})
export class OverviewComponent implements OnInit {

  public barChartOptions = {
    scales: {
      yAxes: [{
        ticks: {
          min: 0
        }
      }]
    },
    legend: {
      display: false
    },
    tooltips: {
      callbacks: {
        label: function (tooltipItem) {
          return tooltipItem.yLabel;
        }
      }
    }
  };

  public gameChartLabels = ['Alle Spiele', 'Verliehene Spiele'];

  public identityCardChartLabels = ['Alle Ausweise', 'Vergebene Ausweise'];

  constructor(public overviewService: OverviewService, private modalService: NgbModal) {
  }

  ngOnInit() {
  }

  public openModal(modalType: string): void {
    switch (modalType) {
      case 'LendIdentityCards':
        this.modalService.open(LendIdentityCardsModalComponent, {size: 'lg'});
        break;
      case 'LendGames':
      default:
        this.modalService.open(LendGamesModalComponent, {size: 'lg'});
    }

  }

}
