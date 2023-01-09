import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OperationsCarComponent } from './operations-car.component';

describe('OperationsCarComponent', () => {
  let component: OperationsCarComponent;
  let fixture: ComponentFixture<OperationsCarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OperationsCarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OperationsCarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
