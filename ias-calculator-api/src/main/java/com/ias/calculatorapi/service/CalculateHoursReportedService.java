package com.ias.calculatorapi.service;

import com.ias.calculatorapi.model.ReportHoursCalculated;
import reactor.core.publisher.Mono;

public interface CalculateHoursReportedService {
    Mono<ReportHoursCalculated> getReportHoursCalculated(String technicianId, Integer weekNumber);
}
