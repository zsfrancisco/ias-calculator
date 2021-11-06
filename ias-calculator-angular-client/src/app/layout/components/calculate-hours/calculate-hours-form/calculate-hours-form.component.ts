import {
  Component,
  EventEmitter,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormGroupDirective,
  Validators,
} from '@angular/forms';
import {
  CalculateHourBodyRequest,
  CalculateHourData,
} from 'src/app/layout/types';
import { CalculateHoursService } from '../../../services/calculate-hours.service';
import { ToastrService } from 'ngx-toastr';
import { Observable, Subscription } from 'rxjs';

@Component({
  selector: 'calculate-hours-form',
  templateUrl: './calculate-hours-form.component.html',
  styleUrls: ['./calculate-hours-form.component.scss'],
})
export class CalculateHoursFormComponent implements OnInit, OnDestroy {
  @Output() hoursCalculated: EventEmitter<CalculateHourData[]> = new EventEmitter<CalculateHourData[]>();
  @Output() calculateHourBodyRequest: EventEmitter<CalculateHourBodyRequest> = new EventEmitter<CalculateHourBodyRequest>();
  form: FormGroup;
  calculateHoursData$: Observable<CalculateHourData[]>;
  calculateHoursDataSubscription: Subscription;
  private _isLoading: boolean = false;
  get isLoading(): boolean {
    return this._isLoading;
  }

  constructor(
    private formBuilder: FormBuilder,
    private calculateHoursService: CalculateHoursService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.initializeForm();
  }

  ngOnDestroy(): void {
    if (this.calculateHoursDataSubscription) this.calculateHoursDataSubscription.unsubscribe();
  }

  private initializeForm() {
    this.form = this.formBuilder.group({
      technicianId: ['', Validators.required],
      weekNumber: [
        '',
        [Validators.required, Validators.min(1), Validators.max(53)],
      ],
    });
  }

  onSubmit(formDirective: FormGroupDirective): void {
    this._isLoading = true;
    if (this.form.invalid) {
      this.toastr.error(
        'Verifique que el formulario esté diligenciado correctamente.'
      );
      this._isLoading = false;
      return Object.values(this.form.controls).forEach((control) => {
        control.markAsTouched();
      });
    }
    const { technicianId, weekNumber } = this.form.value;
    this.getHourCalculated(technicianId, weekNumber, formDirective);
  }

  private getHourCalculated(
    technicianId: string,
    weekNumber: number,
    formDirective: FormGroupDirective
  ): void {
    const request: CalculateHourBodyRequest = {
      technicianId,
      weekNumber,
    };
    this.calculateHoursData$ = this.calculateHoursService.getHoursCalculated(request);
    this.calculateHoursDataSubscription = this.calculateHoursData$.subscribe(
      (response: CalculateHourData[]) => {
        this.calculateHourBodyRequest.emit(this.form.value);
        this.hoursCalculated.emit(response);
      },
      () => {
        this.toastr.error(
          'Ocurrió un error al obtener la información, por favor inténtelo nuevamente'
        );
        this._isLoading = false;
      },
      () => {
        formDirective.resetForm();
        this.form.reset();
        this._isLoading = false;
      }
    );
  }
}
