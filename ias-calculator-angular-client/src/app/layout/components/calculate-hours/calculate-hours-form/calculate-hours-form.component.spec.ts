import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalculateHoursFormComponent } from './calculate-hours-form.component';

describe('CalculateHoursFormComponent', () => {
  let component: CalculateHoursFormComponent;
  let fixture: ComponentFixture<CalculateHoursFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CalculateHoursFormComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CalculateHoursFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
});
