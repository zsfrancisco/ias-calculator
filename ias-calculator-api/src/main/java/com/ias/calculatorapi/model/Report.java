package com.ias.calculatorapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Report {

    @Id
    private String _id;

    @NotNull
    @NotEmpty
    private String technicianId;

    @NotNull
    @NotEmpty
    private String serviceId;

    @NotNull
    private LocalDateTime initTime;

    @NotNull
    private LocalDateTime finishTime;

}
