import {
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import {
  CalculateHourBodyRequest,
  CalculateHourData,
  CalculateHourDisplay,
} from 'src/app/layout/types';

@Component({
  selector: 'calculate-hours-table',
  templateUrl: './calculate-hours-table.component.html',
  styleUrls: ['./calculate-hours-table.component.scss'],
})
export class CalculateHoursTableComponent {
  @Input() hoursCalculated: CalculateHourData[] = [];
  @Input() calculateHourBodyRequest: CalculateHourBodyRequest;
  @Output() makeOtherRequest: EventEmitter<void> = new EventEmitter<void>();
  @ViewChild(MatSort) matSort: MatSort;
  calculateHourDisplay: CalculateHourDisplay[] = [
    { label: 'Tipo de hora', value: 'hourType' },
    { label: 'Cantidad de horas', value: 'value' },
  ];
  get displayedColumns(): string[] {
    return this.calculateHourDisplay.map((hourDisplay) => hourDisplay.value);
  }
  dataSource: MatTableDataSource<CalculateHourData>;

  ngOnChanges(): void {
    this.buildDataSource();
  }

  ngAfterViewInit(): void {
    this.buildDataSource();
  }

  isDataSourceEmpty() {
    return this.hoursCalculated.length ? false : true;
  }

  private buildDataSource() {
    this.dataSource = new MatTableDataSource(this.hoursCalculated);
    this.dataSource.sort = this.matSort;
  }

  onMakeOtherRequest() {
    this.makeOtherRequest.emit(null);
  }
}
