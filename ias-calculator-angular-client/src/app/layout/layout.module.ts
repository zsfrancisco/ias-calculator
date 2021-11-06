import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LayoutRoutingModule } from './layout-routing.module';

import { AngularMaterialModule } from '../shared/angular-material/angular-material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { OwlDateTimeIntl, OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';

import { LayoutComponent } from './layout.component';
import { HeaderComponent } from './components/header/header.component';
import { HandyContentComponent } from './components/handy-content/handy-content.component';
import { RegisterServiceComponent } from './components/register-service/register-service.component';
import { CalculateHoursComponent } from './components/calculate-hours/calculate-hours.component';
import { DefaultInil } from '../shared/DefaultIntl';
import { CalculateHoursFormComponent } from './components/calculate-hours/calculate-hours-form/calculate-hours-form.component';
import { CalculateHoursTableComponent } from './components/calculate-hours/calculate-hours-table/calculate-hours-table.component';

@NgModule({
  declarations: [
    LayoutComponent,
    HeaderComponent,
    HandyContentComponent,
    RegisterServiceComponent,
    CalculateHoursComponent,
    CalculateHoursFormComponent,
    CalculateHoursTableComponent,
  ],
  imports: [
    CommonModule,
    LayoutRoutingModule,
    AngularMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    OwlDateTimeModule,
    OwlNativeDateTimeModule,
  ],
  exports: [LayoutComponent],
  providers: [
    { provide: OwlDateTimeIntl, useClass: DefaultInil }
  ]
})
export class LayoutModule {}
