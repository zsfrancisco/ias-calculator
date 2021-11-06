import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ServiceRequest, ServiceRequestPaths } from '../types';

@Injectable({
  providedIn: 'root',
})
export class RegisterService {
  private _url = environment.url;
  constructor(private http: HttpClient) {}

  createServiceReport(body: ServiceRequest) {
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    };

    return this.http.post(
      `${this._url}${ServiceRequestPaths.REPORTS}`,
      body,
      httpOptions
    );
  }
}
