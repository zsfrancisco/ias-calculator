package com.ias.calculatorapi.controller;

import com.ias.calculatorapi.model.Report;
import com.ias.calculatorapi.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api/v1/reports")
@AllArgsConstructor
public class ReportController {

    private final ReportService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Report> createReport(@Valid @RequestBody Report report) {
        if (!this.validateHours(report.getInitTime(), report.getFinishTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "init hour could be less than finish hour");
        }
        if (!validateRange(report.getInitTime(), report.getFinishTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Range between init and finish hour could be less than 12 hours");
        }
        return service.createReport(report);
    }

    private boolean validateHours(LocalDateTime initHour, LocalDateTime finishHour) {
        return initHour.isBefore(finishHour);
    }

    private boolean validateRange(LocalDateTime initHour, LocalDateTime finishHour) {
        return Duration.between(initHour, finishHour).toMinutes() < 720;
    }

}
