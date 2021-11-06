package com.ias.calculatorapi.controller;

import com.ias.calculatorapi.model.ReportHoursCalculated;
import com.ias.calculatorapi.service.CalculateHoursReportedService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api/v1/calculate-hours")
@AllArgsConstructor
public class CalculateHourController {

    private final CalculateHoursReportedService calculateHoursReportedService;

    @GetMapping("/{technicianId}/week/{weekNumber}")
    public Mono<ReportHoursCalculated> hoursCalculator(@PathVariable String technicianId, @PathVariable Integer weekNumber) {
        return calculateHoursReportedService.getReportHoursCalculated(technicianId, weekNumber);
    }
}
