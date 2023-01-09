import { TestBed } from '@angular/core/testing';

import { ConfirmActionDialogService } from './confirm-action-dialog.service';

describe('ConfirmActionDialogService', () => {
  let service: ConfirmActionDialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConfirmActionDialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
