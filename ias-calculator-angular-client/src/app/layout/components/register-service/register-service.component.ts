import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormGroupDirective,
  Validators,
} from '@angular/forms';
import { ServiceFormValue, ServiceRequest, ServiceResponse } from '../../types';
import * as moment from 'moment';
import { RegisterService } from '../../services/register.service';
moment.locale('es');
import { ToastrService } from 'ngx-toastr';
import { Observable, Subscription } from 'rxjs';

@Component({
  selector: 'app-register-service',
  templateUrl: './register-service.component.html',
  styleUrls: ['./register-service.component.scss'],
})
export class RegisterServiceComponent implements OnInit, OnDestroy {
  form: FormGroup;
  serviceReport$: Observable<ServiceResponse>;
  serviceReportSubscription: Subscription;
  private _isLoading: boolean = false;
  get isLoading(): boolean {
    return this._isLoading;
  }

  constructor(
    private formBuilder: FormBuilder,
    private registerService: RegisterService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.initializeForm();
  }

  ngOnDestroy(): void {
    if (this.serviceReportSubscription) this.serviceReportSubscription.unsubscribe();
  }

  private initializeForm() {
    this.form = this.formBuilder.group({
      technicianId: ['1233189817', Validators.required],
      serviceId: ['10', Validators.required],
      initDate: ['', Validators.required],
      finishDate: ['', Validators.required],
      initHour: ['', Validators.required],
      finishHour: ['', Validators.required],
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
    const requestBody = this.buildRequest(this.form.value);
    this.saveService(requestBody, formDirective);
  }

  private buildRequest(formValue: ServiceFormValue): ServiceRequest {
    const {
      technicianId,
      serviceId,
      initDate,
      finishDate,
      initHour,
      finishHour,
    } = formValue;
    const initTime = this.transformDate(initDate, initHour);
    const finishTime = this.transformDate(finishDate, finishHour);
    return {
      technicianId,
      serviceId,
      initTime,
      finishTime,
    };
  }

  private saveService(
    requestBody: ServiceRequest,
    formDirective: FormGroupDirective
  ) {
    const { initTime, finishTime } = requestBody;
    if (moment(initTime).isSameOrAfter(moment(finishTime))) {
      this._isLoading = false;
      return this.toastr.error(
        'Verifique que la fecha y hora inicial sea antes que la fecha y hora final.'
      );
    }
    this.serviceReport$ = this.registerService.createServiceReport(requestBody) as Observable<ServiceResponse>;
    this.serviceReportSubscription = this.serviceReport$.subscribe(
      (response: ServiceResponse) => {
        this.toastr.success('El servicio ha sido registrado exitosamente.');
      },
      (error) => {
        const { message } = error.error;
        if (
          message ===
          'Range between init and finish hour could be less than 12 hours'
        ) {
          this.toastr.error(
            'Verifique que el rango entre las fechas sea menor o igual a 12 horas e inténtelo nuevamente.'
          );
        } else {
          this.toastr.error(
            'Ocurrió un error al registrar el servicio, por favor inténtelo nuevamente.'
          );
        }
        this._isLoading = false;
      },
      () => {
        formDirective.resetForm();
        this.form.reset();
        this._isLoading = false;
      }
    );
  }

  private transformDate(date: Date, hour: Date): string {
    return `${moment(date).format('YYYY-MM-DD')}T${moment(hour).format(
      'HH:mm'
    )}`;
  }
}
