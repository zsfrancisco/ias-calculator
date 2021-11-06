import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ServiceResponse } from '../../types';
import { RegisterService } from '../../services/register.service';
import { environment } from 'src/environments/environment';
import { HttpResponse } from '@angular/common/http';

describe('RegisterServiceComponent', () => {
  let httpTestingController: HttpTestingController;
  let service: RegisterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [RegisterService],
    });
    httpTestingController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(RegisterService);
  });

  it('should save a new report and return it', () => {
    const reportCreated: ServiceResponse = {
      _id: '1',
      technicianId: '1233189817',
      serviceId: '10',
      initTime: '2021-05-03T07:00:00',
      finishTime: '2021-05-04T09:00:00',
    };

    service
      .createServiceReport(reportCreated)
      .subscribe((data) => expect(data).toEqual(reportCreated), fail);

    const req = httpTestingController.expectOne(
      `${environment.url}/reports`
    );
    expect(req.request.method).toEqual('POST');
    expect(req.request.headers.get('Content-Type')).toEqual('application/json');
    expect(req.request.body).toEqual(reportCreated);

    const expectedResponse = new HttpResponse({
      status: 201,
      statusText: 'Created',
      body: reportCreated,
    });
    req.event(expectedResponse);
  });
});
