import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { CalculateHoursService } from './calculate-hours.service';
import {
  CalculateHourBodyRequest,
  CalculateHourData,
  CalculateHourResponse,
} from '../types';
import { environment } from 'src/environments/environment';

describe('CalculateHoursService', () => {
  let httpTestingController: HttpTestingController;
  let service: CalculateHoursService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CalculateHoursService],
    });

    httpTestingController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(CalculateHoursService);
  });

  it('should return hours calculated for technician and week number', () => {
    const calculateHourResponseMock: CalculateHourResponse = {
      normalHours: 5,
      nightHours: 4,
      sundayHours: 2,
      normalOvertimeHours: 13,
      nightOvertimeHours: 0,
      sundayOvertimeHours: 0,
    };

    const formatedCalculateHourResponseMock: CalculateHourData[] = [
      { hourType: 'Horas normales', value: 5 },
      { hourType: 'Horas nocturnas', value: 4 },
      { hourType: 'Horas dominicales', value: 2 },
      { hourType: 'Horas extras normales', value: 13 },
      { hourType: 'Horas extras nocturnas', value: 0 },
      { hourType: 'Horas extras dominicales', value: 0 },
    ];

    const hoursRequest: CalculateHourBodyRequest = {
      technicianId: '1233189817',
      weekNumber: 18,
    };

    service.getHoursCalculated(hoursRequest).subscribe((data) => {
      expect(data).toEqual(formatedCalculateHourResponseMock), fail;
    });
    const req = httpTestingController.expectOne(
      `${environment.url}/calculate-hours/1233189817/week/18`
    );

    expect(req.request.method).toBe('GET');
    req.flush(calculateHourResponseMock);
  });
});
