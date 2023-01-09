import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OperationBuyDetailsComponent } from './operation-buy-details.component';

describe('BuySummaryComponent', () => {
  let component: OperationBuyDetailsComponent;
  let fixture: ComponentFixture<OperationBuyDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OperationBuyDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OperationBuyDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
