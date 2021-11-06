package com.ias.calculatorapi.service;

import com.ias.calculatorapi.model.Report;
import com.ias.calculatorapi.repository.ReportRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReportServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReportRepository repository;

    @Test
    public void whenReportIsCorrect_thenReturnReportCreated() {
        Report report = Report.builder()
                .technicianId("1")
                .serviceId("1")
                .initTime(LocalDateTime.parse("2021-04-29T14:00"))
                .finishTime(LocalDateTime.parse("2021-04-29T15:00"))
                .build();
        Mockito.when(repository.save(report)).thenReturn(Mono.just(report));
        webTestClient
                .post()
                .uri("/api/v1/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(report), Report.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Report.class)
                .consumeWith(reportEntityExchangeResult -> {
                    Report reportCreated = reportEntityExchangeResult.getResponseBody();
                    assert reportCreated != null;
                    Assertions.assertEquals(reportCreated.getTechnicianId(), "1");
                })
        ;
    }

    @Test
    public void whenInitHourIsGreaterThanFinishHour_thenReturnDadRequest() {
        Report report = Report.builder()
                .technicianId("1")
                .serviceId("1")
                .initTime(LocalDateTime.parse("2021-05-29T14:00"))
                .finishTime(LocalDateTime.parse("2021-04-29T15:00"))
                .build();
        Mockito.when(repository.save(report)).thenReturn(Mono.just(report));
        webTestClient
                .post()
                .uri("/api/v1/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(report), Report.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

}