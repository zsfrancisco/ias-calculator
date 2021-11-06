package com.ias.calculatorapi.service;

import com.ias.calculatorapi.model.Report;
import com.ias.calculatorapi.repository.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository repository;

    @Override
    public Mono<Report> createReport(Report report) {
        return repository.save(report);
    }

}
