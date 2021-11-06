package com.ias.calculatorapi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportHoursCalculated {
    private double normalHours;
    private double nightHours;
    private double sundayHours;
    private double normalOverTimeHours;
    private double nightOvertimeHours;
    private double sundayOvertimeHours;
}
