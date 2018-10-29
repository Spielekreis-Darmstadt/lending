import { TestBed, inject } from '@angular/core/testing';

import { MultipleGameActivationModelService } from './multiple-game-activation-model.service';

describe('MultipleGameActivationModelService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MultipleGameActivationModelService]
    });
  });

  it('should be created', inject([MultipleGameActivationModelService], (service: MultipleGameActivationModelService) => {
    expect(service).toBeTruthy();
  }));
});
