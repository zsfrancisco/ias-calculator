import { Component } from '@angular/core';
import { TabRoute } from '../../types/handy-content.interfaces';

@Component({
  selector: 'handy-content',
  templateUrl: './handy-content.component.html',
  styleUrls: ['./handy-content.component.scss'],
})
export class HandyContentComponent {
  private _tabRouteList: TabRoute[] = [
    {
      label: 'Registrar servicio',
      icon: 'material-icons-outlined',
      route: 'register',
    },
    {
      label: 'Calcular horas trabajadas',
      icon: 'material-icons-outlined',
      route: 'calculate',
    },
  ];

  get tabRouteList(): TabRoute[] {
    return this._tabRouteList;
  }
}
