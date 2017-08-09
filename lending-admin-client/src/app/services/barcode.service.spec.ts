import { TestBed, inject } from '@angular/core/testing';

import { BarcodeService } from './barcode.service';

describe('BarcodeService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BarcodeService]
    });
  });

  it('should be created', inject([BarcodeService], (service: BarcodeService) => {
    expect(service).toBeTruthy();
  }));
});
