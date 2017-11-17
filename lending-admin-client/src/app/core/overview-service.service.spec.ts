import { TestBed, inject } from '@angular/core/testing';

import { OverviewServiceService } from './overview.service';

describe('OverviewServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OverviewServiceService]
    });
  });

  it('should be created', inject([OverviewServiceService], (service: OverviewServiceService) => {
    expect(service).toBeTruthy();
  }));
});
