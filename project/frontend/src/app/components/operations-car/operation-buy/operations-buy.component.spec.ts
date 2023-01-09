import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OperationsBuyComponent } from './operations-buy.component';

describe('OperationsCarComponent', () => {
  let component: OperationsBuyComponent;
  let fixture: ComponentFixture<OperationsBuyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OperationsBuyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OperationsBuyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
