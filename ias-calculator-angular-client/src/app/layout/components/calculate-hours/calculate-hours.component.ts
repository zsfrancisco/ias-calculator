import { Component, OnInit } from '@angular/core';
import { CalculateHourBodyRequest, CalculateHourData } from '../../types';

@Component({
  selector: 'calculate-hours',
  templateUrl: './calculate-hours.component.html',
  styleUrls: ['./calculate-hours.component.scss'],
})
export class CalculateHoursComponent implements OnInit {
  private _hoursCalculated: CalculateHourData[] = [];
  public get hoursCalculated(): CalculateHourData[] {
    return this._hoursCalculated;
  }
  private _calculateHourBodyRequest: CalculateHourBodyRequest;
  public get calculateHourBodyRequest(): CalculateHourBodyRequest {
    return this._calculateHourBodyRequest;
  }
  private _showForm = true;
  public get showForm(): boolean {
    return this._showForm;
  }

  constructor() {}

  ngOnInit(): void {}

  onHoursCalculated(hoursCalculated: CalculateHourData[]) {
    this._hoursCalculated = hoursCalculated;
    this._showForm = false;
  }

  onCalculateHourBodyRequest(calculateHourBodyRequest: CalculateHourBodyRequest) {
    this._calculateHourBodyRequest = calculateHourBodyRequest;
  }

  onMakeOtherRequest() {
    this._showForm = true;
    this._hoursCalculated = [];
    this._calculateHourBodyRequest = null;
  }
}
