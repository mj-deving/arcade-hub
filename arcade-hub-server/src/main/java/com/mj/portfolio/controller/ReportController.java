package com.mj.portfolio.controller;

import com.mj.portfolio.dto.DailyReportResponse;
import com.mj.portfolio.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "Reports", description = "Aggregated daily reports for revenue and compliance auditing")
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
    @Operation(summary = "Get daily report", description = "Aggregated revenue, errors, maintenance, and visitor stats for a location on a specific date")
    @GetMapping("/daily")
    public DailyReportResponse daily(
            @Parameter(description = "Location to report on") @RequestParam UUID locationId,
            @Parameter(description = "Report date (ISO format: yyyy-MM-dd)", example = "2026-02-16") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.getDailyReport(locationId, date);
    }
}
