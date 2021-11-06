import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalculateHoursTableComponent } from './calculate-hours-table.component';

describe('CalculateHoursTableComponent', () => {
  let component: CalculateHoursTableComponent;
  let fixture: ComponentFixture<CalculateHoursTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CalculateHoursTableComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CalculateHoursTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
});
