import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterServiceComponent } from './components/register-service/register-service.component';
import { CalculateHoursComponent } from './components/calculate-hours/calculate-hours.component';
import { HandyContentComponent } from './components/handy-content/handy-content.component';

const routes: Routes = [
  {
    path: '',
    component: HandyContentComponent,
    children: [
      {
        path: '',
        redirectTo: 'register',
        pathMatch: 'full',
      },
      {
        path: 'register',
        component: RegisterServiceComponent,
        data: {
          label: 'register',
        },
      },
      {
        path: 'calculate',
        component: CalculateHoursComponent,
        data: {
          label: 'calculate',
        },
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LayoutRoutingModule {}
