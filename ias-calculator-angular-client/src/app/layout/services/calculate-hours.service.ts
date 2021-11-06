import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import {
  CalculateHourBodyRequest,
  CalculateHourData,
  CalculateHourResponse,
  CalculateHoursRequestPaths,
  HourTypesInSpanish,
} from '../types';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class CalculateHoursService {
  private _url = environment.url;
  constructor(private http: HttpClient) {}

  getHoursCalculated(calculateHourBodyRequest: CalculateHourBodyRequest) {
    const { technicianId, weekNumber } = calculateHourBodyRequest;
    return this.http
      .get(
        `${this._url}${CalculateHoursRequestPaths.CALCULATE}/${technicianId}/week/${weekNumber}`
      )
      .pipe(
        map((hoursCalculated: CalculateHourResponse) => {
          if (hoursCalculated) {
            const calculatedHours: CalculateHourData[] = [];
            const keys = Object.keys(hoursCalculated);
            for (const key of keys) {
              const keyForEnum = key.toUpperCase();
              calculatedHours.push({
                hourType: HourTypesInSpanish[keyForEnum],
                value: hoursCalculated[key],
              });
            }
            return calculatedHours;
          }
        })
      );
  }
}
