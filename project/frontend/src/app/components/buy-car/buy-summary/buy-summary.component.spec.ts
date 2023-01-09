import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuySummaryComponent } from './buy-summary.component';

describe('BuySummaryComponent', () => {
  let component: BuySummaryComponent;
  let fixture: ComponentFixture<BuySummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BuySummaryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BuySummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
