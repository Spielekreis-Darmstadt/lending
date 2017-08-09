import { TestBed, inject } from '@angular/core/testing';

import { AddGameService } from './add-game.service';

describe('AddGameService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AddGameService]
    });
  });

  it('should be created', inject([AddGameService], (service: AddGameService) => {
    expect(service).toBeTruthy();
  }));
});
