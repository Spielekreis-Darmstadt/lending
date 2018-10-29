import { TestBed, inject } from '@angular/core/testing';

import { MultipleGameAdditionModelService } from './multiple-game-addition-model.service';

describe('MultipleGameAdditionModelService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MultipleGameAdditionModelService]
    });
  });

  it('should be created', inject([MultipleGameAdditionModelService], (service: MultipleGameAdditionModelService) => {
    expect(service).toBeTruthy();
  }));
});
