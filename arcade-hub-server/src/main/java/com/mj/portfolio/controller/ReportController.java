package com.mj.portfolio.controller;

import com.mj.portfolio.dto.DailyReportResponse;
import com.mj.portfolio.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/arcade/api/reports")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    /**
     * Returns aggregated daily stats for a location.
     * Example: GET /arcade/api/reports/daily?locationId=uuid&date=2026-02-14
     *
     * @DateTimeFormat(iso = ISO.DATE) is required: Spring cannot parse LocalDate
     * from a query string without it, even with parameters=true enabled.
     */
    @GetMapping("/daily")
    public DailyReportResponse daily(
            @RequestParam UUID locationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.getDailyReport(locationId, date);
    }
}
