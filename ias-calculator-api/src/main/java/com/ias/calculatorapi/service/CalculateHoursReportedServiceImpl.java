package com.ias.calculatorapi.service;

import com.ias.calculatorapi.model.Report;
import com.ias.calculatorapi.model.ReportHoursCalculated;
import com.ias.calculatorapi.repository.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class CalculateHoursReportedServiceImpl implements CalculateHoursReportedService {

    private final LocalTime INIT_TIME_LIMIT = LocalTime.of(7, 0);
    private final LocalTime FINISH_TIME_LIMIT = LocalTime.of(20, 0);
    private final LocalTime INIT_HOURS_DAY = LocalTime.MIN;
    private final LocalTime FINISH_HOURS_DAY = LocalTime.MAX;

    private final Double INIT_OVERTIME_HOURS = 48D;
    private final AtomicReference<Double> TOTAL_HOURS_WORKED = new AtomicReference<>();

    private final ReportRepository reportRepository;

    @Override
    public Mono<ReportHoursCalculated> getReportHoursCalculated(String technicianId, Integer weekNumber) {

        TOTAL_HOURS_WORKED.set(0d);

        final ReportHoursCalculated reportHoursCalculated = ReportHoursCalculated.builder()
                .normalHours(0)
                .nightHours(0)
                .sundayHours(0)
                .normalOverTimeHours(0)
                .nightOvertimeHours(0)
                .sundayOvertimeHours(0)
                .build();

        AtomicReference<Double> totalMinutesWorked = new AtomicReference<>((double) 0);

        return reportRepository.findReportByTechnicianId(technicianId)
                .filter(report -> {
                    return isReportDateInsideWeek(report.getInitTime(), weekNumber)
                            || isReportDateInsideWeek(report.getFinishTime(), weekNumber);
                })
                .map(report -> {
                    totalMinutesWorked.set(0.0);
                    totalMinutesWorked.updateAndGet(minutesWorked ->
                            minutesWorked + this.getTotalMinutesWorked(report.getInitTime(), report.getFinishTime(), weekNumber));

                    Double[] totalMinutesWorkedInSunday = getMinutesWorkedInSundayHours(Flux.just(report), weekNumber);
                    Double[] totalMinutesWorkedInNormal = getMinutesWorkedInNormalHours(Flux.just(report), weekNumber);

                    Double timeMinutesRemaining = totalMinutesWorked.get() - (totalMinutesWorkedInNormal[0] + totalMinutesWorkedInSunday[0]
                            + totalMinutesWorkedInNormal[1] + totalMinutesWorkedInSunday[1]);
                    Double[] totalMinutesWorkedInNight = getMinutesWorkedInNightHours(timeMinutesRemaining);

                    reportHoursCalculated.setNormalHours(reportHoursCalculated.getNormalHours() + transformMinutesToHours(totalMinutesWorkedInNormal[0]));
                    reportHoursCalculated.setNormalOverTimeHours(reportHoursCalculated.getNormalOverTimeHours() + transformMinutesToHours(totalMinutesWorkedInNormal[1]));

                    reportHoursCalculated.setSundayHours(reportHoursCalculated.getSundayHours() + transformMinutesToHours(totalMinutesWorkedInSunday[0]));
                    reportHoursCalculated.setSundayOvertimeHours(reportHoursCalculated.getSundayOvertimeHours() + transformMinutesToHours(totalMinutesWorkedInSunday[1]));

                    reportHoursCalculated.setNightHours(reportHoursCalculated.getNightHours() + transformMinutesToHours(totalMinutesWorkedInNight[0]));
                    reportHoursCalculated.setNightOvertimeHours(reportHoursCalculated.getNightOvertimeHours() + transformMinutesToHours(totalMinutesWorkedInNight[1]));

                    System.out.println("reportHoursCalculated = " + reportHoursCalculated);
                    return report;
                })
                .then(Mono.just(reportHoursCalculated));
    }

    private Double[] getMinutesWorkedInSundayHours(Flux<Report> reportFlux, Integer weekNumber) {
        AtomicReference<Double> totalMinutesInSunday = new AtomicReference<>((double) 0);
        AtomicReference<Double> totalMinutesOvertimeInSunday = new AtomicReference<>((double) 0);

        reportFlux.filter(report -> {
            return isDateInSunday(report.getInitTime()) || isDateInSunday(report.getFinishTime());
        })
                .map(report -> {
                    LocalDateTime initTime = report.getInitTime();
                    LocalDateTime finishTime = report.getFinishTime();

                    TemporalField weekOfYear = WeekFields.of(Locale.FRANCE).weekOfWeekBasedYear();

                    if (
                            (weekNumber == initTime.get(weekOfYear) && weekNumber < finishTime.get(weekOfYear))
                                    ||
                                    (weekNumber < initTime.get(weekOfYear) && weekNumber == finishTime.get(weekOfYear))
                                    ||
                                    (weekNumber == initTime.get(weekOfYear) && weekNumber == finishTime.get(weekOfYear))

                    ) {


                        if (!isDateInSunday(initTime) && isDateInSunday(finishTime)) {

                            totalMinutesInSunday
                                    .updateAndGet(minutesInSunday ->
                                            minutesInSunday + getMinutesBetweenTwoDates(INIT_HOURS_DAY, finishTime.toLocalTime()));

                        } else if (isDateInSunday(initTime) && isDateInSunday(finishTime)) {

                            totalMinutesInSunday
                                    .updateAndGet(minutesInSunday ->
                                            minutesInSunday + getMinutesBetweenTwoDates(initTime.toLocalTime(), finishTime.toLocalTime()));
                        } else {

                            totalMinutesInSunday
                                    .updateAndGet(minutesInSunday ->
                                            minutesInSunday + getMinutesBetweenTwoDates(initTime.toLocalTime(), FINISH_HOURS_DAY));
                        }
                    }
                    return report;
                })
                .subscribe();

        TOTAL_HOURS_WORKED.updateAndGet(totalHours -> totalHours + transformMinutesToHours(totalMinutesInSunday.get()));
        if (isTotalHoursWorkedOvertime()) {
            double overtimeHours = (TOTAL_HOURS_WORKED.get() - INIT_OVERTIME_HOURS) * 60;

            totalMinutesInSunday.updateAndGet(minutesInNormal -> minutesInNormal - overtimeHours);
            totalMinutesOvertimeInSunday.updateAndGet(normalOvertime -> normalOvertime + overtimeHours);
            TOTAL_HOURS_WORKED.set(48D);
        }
        Double[] resultNormalHours = new Double[]{totalMinutesInSunday.get(), totalMinutesOvertimeInSunday.get()};
        return resultNormalHours;
    }

    private Double[] getMinutesWorkedInNormalHours(Flux<Report> reportFlux, Integer weekNumber) {
        AtomicReference<Double> totalMinutesInNormalHours = new AtomicReference<>((double) 0);
        AtomicReference<Double> totalMinutesOverTimeInNormalHours = new AtomicReference<>((double) 0);

        reportFlux
                .filter(report -> {
                    return isDateInNormalJourney(report.getInitTime().toLocalTime(), report.getFinishTime().toLocalTime());
                })
                .map(report -> {
                    LocalTime initTime = report.getInitTime().toLocalTime();
                    LocalTime finishTime = report.getFinishTime().toLocalTime();

                    TemporalField weekOfYear = WeekFields.of(Locale.FRANCE).weekOfWeekBasedYear();

                    if (!isDateInSunday(report.getInitTime())) {

                        if (initTime.compareTo(FINISH_TIME_LIMIT) >= 0
                                && initTime.compareTo(finishTime) > 0
                                && finishTime.compareTo(INIT_TIME_LIMIT) >= 0) {

                            totalMinutesInNormalHours.updateAndGet(minutesInNormalJourney ->
                                    getMinutesBetweenTwoDates(INIT_TIME_LIMIT, finishTime));

                        } else if (initTime.compareTo(INIT_TIME_LIMIT) >= 0
                                && initTime.compareTo(finishTime) < 0
                                && finishTime.compareTo(FINISH_TIME_LIMIT) < 0) {

                            totalMinutesInNormalHours.updateAndGet(minutesInNormalJourney ->
                                    getMinutesBetweenTwoDates(initTime, finishTime));

                        } else if ((initTime.compareTo(INIT_TIME_LIMIT) >= 0
                                && initTime.compareTo(FINISH_TIME_LIMIT) < 0
                                && finishTime.compareTo(FINISH_TIME_LIMIT) > 0
                                && finishTime.compareTo(FINISH_HOURS_DAY) < 0)
                                ||
                                (initTime.compareTo(INIT_TIME_LIMIT) >= 0
                                        && initTime.compareTo(FINISH_TIME_LIMIT) < 0
                                        && initTime.compareTo(finishTime) > 0
                                        && finishTime.compareTo(INIT_TIME_LIMIT) < 0)) {

                            totalMinutesInNormalHours.updateAndGet(minutesInNormalJourney ->
                                    getMinutesBetweenTwoDates(initTime, FINISH_TIME_LIMIT));

                        } else if (initTime.compareTo(INIT_TIME_LIMIT) < 0
                                && initTime.compareTo(finishTime) < 0
                                && finishTime.compareTo(INIT_TIME_LIMIT) >= 0) {

                            totalMinutesInNormalHours.updateAndGet(minutesInNormalJourney ->
                                    getMinutesBetweenTwoDates(INIT_TIME_LIMIT, finishTime));

                        }

                    } else if (isDateInSunday(report.getInitTime())
                            && weekNumber > report.getInitTime().get(weekOfYear)
                            && finishTime.compareTo(INIT_TIME_LIMIT) >= 0) {
                        totalMinutesInNormalHours.updateAndGet(minutesInNormalJourney ->
                                getMinutesBetweenTwoDates(INIT_TIME_LIMIT, finishTime));
                    }

                    return report;
                })
                .subscribe();

        TOTAL_HOURS_WORKED.updateAndGet(totalHours -> totalHours + transformMinutesToHours(totalMinutesInNormalHours.get()));
        if (isTotalHoursWorkedOvertime()) {
            double overtimeHours = (TOTAL_HOURS_WORKED.get() - INIT_OVERTIME_HOURS) * 60;

            totalMinutesInNormalHours.updateAndGet(minutesInNormal -> minutesInNormal - overtimeHours);
            totalMinutesOverTimeInNormalHours.updateAndGet(normalOvertime -> normalOvertime + overtimeHours);
            TOTAL_HOURS_WORKED.set(48D);
        }
        Double[] resultNormalHours = new Double[]{totalMinutesInNormalHours.get(), totalMinutesOverTimeInNormalHours.get()};
        return resultNormalHours;
    }

    private Double[] getMinutesWorkedInNightHours(Double timeMinutesRemaining) {
        TOTAL_HOURS_WORKED.updateAndGet(totalHours -> totalHours + transformMinutesToHours(timeMinutesRemaining));
        Double[] resultNightMinutes = new Double[]{0D, 0D};
        if (isTotalHoursWorkedOvertime()) {
            resultNightMinutes[1] = timeMinutesRemaining;
            TOTAL_HOURS_WORKED.set(48D);
        } else {
            resultNightMinutes[0] = timeMinutesRemaining;
        }
        return resultNightMinutes;
    }

    private double transformMinutesToHours(double minutes) {
        return Math.round(minutes / 60);
    }

    private boolean isDateInSunday(LocalDateTime date) {
        return date.getDayOfWeek().toString().equals("SUNDAY");
    }

    private boolean isDateInNormalJourney(LocalTime initDate, LocalTime finishDate) {
        return initDate.compareTo(INIT_TIME_LIMIT) >= 0 || finishDate.compareTo(FINISH_TIME_LIMIT) <= 0;
    }

    private boolean isReportDateInsideWeek(LocalDateTime date, Integer weekNumber) {
        TemporalField weekOfYear = WeekFields.of(Locale.FRANCE).weekOfWeekBasedYear();
        return date.get(weekOfYear) == weekNumber;
    }

    private double getMinutesBetweenTwoDates(LocalTime initDate, LocalTime finishDate) {
        return Duration.between(initDate, finishDate).toMinutes();
    }

    private double getTotalMinutesWorked(LocalDateTime initDate, LocalDateTime finishDate, Integer weekNumber) {
        TemporalField weekOfYear = WeekFields.of(Locale.FRANCE).weekOfWeekBasedYear();
        Integer weekOfInitDate = initDate.get(weekOfYear);
        Integer weekOfFinishDate = finishDate.get(weekOfYear);
        if (weekOfInitDate != weekOfFinishDate && weekOfInitDate == weekNumber) {
            return Duration.between(initDate.toLocalTime(), FINISH_HOURS_DAY).toMinutes();
        } else if (weekOfInitDate != weekOfFinishDate && weekOfFinishDate == weekNumber) {
            return Duration.between(INIT_HOURS_DAY, finishDate.toLocalTime()).toMinutes();
        }
        return Duration.between(initDate, finishDate).toMinutes();
    }

    private boolean isTotalHoursWorkedOvertime() {
        return Double.parseDouble(String.valueOf(TOTAL_HOURS_WORKED)) >= INIT_OVERTIME_HOURS;
    }

}
