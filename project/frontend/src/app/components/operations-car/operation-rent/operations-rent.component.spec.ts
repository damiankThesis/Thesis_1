import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OperationsRentComponent } from './operations-rent.component';

describe('OperationsCarComponent', () => {
  let component: OperationsRentComponent;
  let fixture: ComponentFixture<OperationsRentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OperationsRentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OperationsRentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
