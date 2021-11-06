package com.ias.calculatorapi.service;

import com.ias.calculatorapi.model.Report;
import reactor.core.publisher.Mono;

public interface ReportService {
    Mono<Report> createReport(Report report);
}
