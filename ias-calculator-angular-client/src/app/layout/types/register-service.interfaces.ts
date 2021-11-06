interface ServiceRequest {
  technicianId: string;
  serviceId: string;
  initTime: string;
  finishTime: string;
}

interface ServiceFormValue {
  technicianId: string;
  serviceId: string;
  initDate: Date;
  finishDate: Date;
  initHour: Date;
  finishHour: Date;
}

interface ServiceResponse {
  _id?: String;
  technicianId: string;
  serviceId: string;
  initTime: string;
  finishTime: string;
}

interface saveServiceResponse {
  successStatus: boolean;
  message: string;
}

enum ServiceRequestPaths {
  REPORTS = '/reports',
}

export {
  ServiceRequest,
  ServiceFormValue,
  ServiceRequestPaths,
  ServiceResponse,
  saveServiceResponse,
};
