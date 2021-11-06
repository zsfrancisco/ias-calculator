interface CalculateHourResponse {
  normalHours: number;
  sundayHours: number;
  nightHours: number;
  normalOvertimeHours: number;
  sundayOvertimeHours: number;
  nightOvertimeHours: number;
}

interface CalculateHourData {
  hourType: string;
  value: number;
}

interface CalculateHourBodyRequest {
  technicianId: string;
  weekNumber: number;
}

interface CalculateHourDisplay {
  label: string;
  value: string;
}

enum CalculateHoursRequestPaths {
  CALCULATE = '/calculate-hours',
}

enum HourTypesInSpanish {
  NORMALHOURS = 'Horas normales',
  NIGHTHOURS = 'Horas nocturnas',
  SUNDAYHOURS = 'Horas dominicales',
  NORMALOVERTIMEHOURS = 'Horas extras normales',
  NIGHTOVERTIMEHOURS = 'Horas extras nocturnas',
  SUNDAYOVERTIMEHOURS = 'Horas extras dominicales',
}

export {
  CalculateHourResponse,
  CalculateHourData,
  CalculateHourBodyRequest,
  CalculateHourDisplay,
  CalculateHoursRequestPaths,
  HourTypesInSpanish,
};
