package com.ias.calculatorapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ias.calculatorapi.model.Report;
import com.ias.calculatorapi.model.ReportHoursCalculated;
import com.ias.calculatorapi.repository.ReportRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.*;

@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalculateHoursReportedServiceTest {

    private final String technicianId = "1233189817";
    private final String serviceId = "10";
    private final String weekNumber = "18";

    @Autowired
    private WebTestClient client;

    @MockBean
    private ReportRepository serviceReportRepository;

    @BeforeEach
    public void setup() {

        List<Report> list = new ArrayList<>();

        Report report1 = Report.builder()
                .technicianId(technicianId)
                .serviceId(serviceId)
                .initTime(LocalDateTime.parse("2021-05-08T18:00:00"))
                .finishTime(LocalDateTime.parse("2021-05-09T04:00:00"))
                .build();
        list.add(report1);

        Report report2 = Report.builder()
                .technicianId(technicianId)
                .serviceId(serviceId)
                .initTime(LocalDateTime.parse("2021-05-08T22:00:00"))
                .finishTime(LocalDateTime.parse("2021-05-09T05:00:00"))
                .build();
        list.add(report2);

        Report report3 = Report.builder()
                .technicianId(technicianId)
                .serviceId(serviceId)
                .initTime(LocalDateTime.parse("2021-05-09T06:00:00"))
                .finishTime(LocalDateTime.parse("2021-05-09T09:00:00"))
                .build();
        list.add(report3);

        Report report4 = Report.builder()
                .technicianId(technicianId)
                .serviceId(serviceId)
                .initTime(LocalDateTime.parse("2021-05-02T22:00:00"))
                .finishTime(LocalDateTime.parse("2021-05-03T04:00:00"))
                .build();
        list.add(report4);

        Report report5 = Report.builder()
                .technicianId(technicianId)
                .serviceId(serviceId)
                .initTime(LocalDateTime.parse("2021-05-02T22:00:00"))
                .finishTime(LocalDateTime.parse("2021-05-03T09:00:00"))
                .build();
        list.add(report5);

        Report report6 = Report.builder()
                .technicianId(technicianId)
                .serviceId(serviceId)
                .initTime(LocalDateTime.parse("2021-05-09T22:00:00"))
                .finishTime(LocalDateTime.parse("2021-05-10T04:00:00"))
                .build();
        list.add(report6);

        Report report7 = Report.builder()
                .technicianId(technicianId)
                .serviceId(serviceId)
                .initTime(LocalDateTime.parse("2021-05-09T22:00:00"))
                .finishTime(LocalDateTime.parse("2021-05-10T09:00:00"))
                .build();
        list.add(report7);

        Report report8 = Report.builder()
                .technicianId(technicianId)
                .serviceId(serviceId)
                .initTime(LocalDateTime.parse("2021-05-03T08:00:00"))
                .finishTime(LocalDateTime.parse("2021-05-03T19:00:00"))
                .build();
        list.add(report8);

        Report report9 = Report.builder()
                .technicianId(technicianId)
                .serviceId(serviceId)
                .initTime(LocalDateTime.parse("2021-05-03T22:00:00"))
                .finishTime(LocalDateTime.parse("2021-05-03T23:00:00"))
                .build();
        list.add(report9);

        Report report10 = Report.builder()
                .technicianId(technicianId)
                .serviceId(serviceId)
                .initTime(LocalDateTime.parse("2021-05-03T22:00:00"))
                .finishTime(LocalDateTime.parse("2021-05-04T03:00:00"))
                .build();
        list.add(report10);

        Report report11 = Report.builder()
                .technicianId(technicianId)
                .serviceId(serviceId)
                .initTime(LocalDateTime.parse("2021-05-03T03:00:00"))
                .finishTime(LocalDateTime.parse("2021-05-03T04:00:00"))
                .build();
        list.add(report11);

        Report report12 = Report.builder()
                .technicianId(technicianId)
                .serviceId(serviceId)
                .initTime(LocalDateTime.parse("2021-05-03T17:00:00"))
                .finishTime(LocalDateTime.parse("2021-05-03T22:00:00"))
                .build();
        list.add(report12);

        Report report13 = Report.builder()
                .technicianId(technicianId)
                .serviceId(serviceId)
                .initTime(LocalDateTime.parse("2021-05-03T18:00:00"))
                .finishTime(LocalDateTime.parse("2021-05-04T03:00:00"))
                .build();
        list.add(report13);

        Report report14 = Report.builder()
                .technicianId(technicianId)
                .serviceId(serviceId)
                .initTime(LocalDateTime.parse("2021-05-03T22:00:00"))
                .finishTime(LocalDateTime.parse("2021-05-04T08:00:00"))
                .build();
        list.add(report14);

        Report report15 = Report.builder()
                .technicianId(technicianId)
                .serviceId(serviceId)
                .initTime(LocalDateTime.parse("2021-05-03T03:00:00"))
                .finishTime(LocalDateTime.parse("2021-05-03T08:00:00"))
                .build();
        list.add(report15);

        Flux<Report> serviceReportFlux = Flux.fromIterable(list);
        Mockito.when(serviceReportRepository.findReportByTechnicianId(technicianId))
                .thenReturn(serviceReportFlux);
    }

    @Test
    public void ifDataExists_thenGetCalculatedHours() {
        client.get().uri("/api/v1/calculate-hours/" + technicianId + "/week/" + weekNumber)
                .exchange()
                .expectStatus().isOk()
                .expectBody()

                .jsonPath("$.normalHours").isEqualTo(15)
                .jsonPath("$.nightHours").isEqualTo(17)
                .jsonPath("$.sundayHours").isEqualTo(16)
                .jsonPath("$.normalOverTimeHours").isEqualTo(7)
                .jsonPath("$.nightOvertimeHours").isEqualTo(29)
                .jsonPath("$.sundayOvertimeHours").isEqualTo(0);
    }

    @Test
    public void ifNoDataExists_thenGetZeroCalculatedHours() {
        client.get().uri("/api/v1/calculate-hours/1233189817/week/153")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.normalHours").isEqualTo(0)
                .jsonPath("$.nightHours").isEqualTo(0)
                .jsonPath("$.sundayHours").isEqualTo(0)
                .jsonPath("$.normalOverTimeHours").isEqualTo(0)
                .jsonPath("$.nightOvertimeHours").isEqualTo(0)
                .jsonPath("$.sundayOvertimeHours").isEqualTo(0);
    }

}