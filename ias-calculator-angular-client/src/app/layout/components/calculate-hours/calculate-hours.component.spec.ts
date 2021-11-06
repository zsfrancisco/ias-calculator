import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalculateHoursComponent } from './calculate-hours.component';

describe('CalculateHoursComponent', () => {
  let component: CalculateHoursComponent;
  let fixture: ComponentFixture<CalculateHoursComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CalculateHoursComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CalculateHoursComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
});
