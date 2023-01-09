import { TestBed } from '@angular/core/testing';

import { RentAndBuyService } from './rentAndBuy.service';

describe('RentAndBuyService', () => {
  let service: RentAndBuyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RentAndBuyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
