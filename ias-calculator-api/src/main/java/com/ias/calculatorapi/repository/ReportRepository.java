package com.ias.calculatorapi.repository;

import com.ias.calculatorapi.model.Report;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public interface ReportRepository extends ReactiveMongoRepository<Report, String> {

    Flux<Report> findReportByTechnicianId(String id);

}
