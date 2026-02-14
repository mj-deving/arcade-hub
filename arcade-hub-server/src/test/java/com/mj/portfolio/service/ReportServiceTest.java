package com.mj.portfolio.service;

import com.mj.portfolio.dto.DailyReportResponse;
import com.mj.portfolio.entity.Location;
import com.mj.portfolio.entity.enums.AccessEventType;
import com.mj.portfolio.entity.enums.MachineEventType;
import com.mj.portfolio.repository.AccessEventRepository;
import com.mj.portfolio.repository.MachineEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Plain unit test for ReportService aggregation logic.
 * Verifies correct netRevenue calculation and null-SUM BigDecimal.ZERO fallback.
 */
class ReportServiceTest {

    private ReportService service;
    private MachineEventRepository machineEventRepo;
    private AccessEventRepository accessEventRepo;
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        machineEventRepo = mock(MachineEventRepository.class);
        accessEventRepo = mock(AccessEventRepository.class);
        locationService = mock(LocationService.class);
        service = new ReportService(machineEventRepo, accessEventRepo, locationService);
    }

    @Test
    void netRevenue_isCorrect() {
        UUID locationId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2026, 2, 14);

        Location loc = new Location();
        loc.setName("Main Hall");
        when(locationService.getOrThrow(locationId)).thenReturn(loc);

        when(machineEventRepo.sumValueByLocationAndTypeAndTimeRange(
                eq(locationId), eq(MachineEventType.COIN_IN), any(), any()))
                .thenReturn(new BigDecimal("100.00"));
        when(machineEventRepo.sumValueByLocationAndTypeAndTimeRange(
                eq(locationId), eq(MachineEventType.COIN_OUT), any(), any()))
                .thenReturn(new BigDecimal("30.00"));
        when(machineEventRepo.countByLocationAndTypeAndTimeRange(any(), any(), any(), any()))
                .thenReturn(0L);
        when(accessEventRepo.countByLocationAndTypeAndTimeRange(any(), any(), any(), any()))
                .thenReturn(0L);

        DailyReportResponse report = service.getDailyReport(locationId, date);

        assertEquals(new BigDecimal("100.00"), report.getTotalCoinIn());
        assertEquals(new BigDecimal("30.00"), report.getTotalCoinOut());
        assertEquals(new BigDecimal("70.00"), report.getNetRevenue());
    }

    @Test
    void nullSumFromDb_fallsBackToZero() {
        UUID locationId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2026, 2, 14);

        Location loc = new Location();
        loc.setName("Empty Hall");
        when(locationService.getOrThrow(locationId)).thenReturn(loc);

        // Database SUM returns null when no rows match the time range
        when(machineEventRepo.sumValueByLocationAndTypeAndTimeRange(any(), any(), any(), any()))
                .thenReturn(null);
        when(machineEventRepo.countByLocationAndTypeAndTimeRange(any(), any(), any(), any()))
                .thenReturn(0L);
        when(accessEventRepo.countByLocationAndTypeAndTimeRange(any(), any(), any(), any()))
                .thenReturn(0L);

        DailyReportResponse report = service.getDailyReport(locationId, date);

        assertEquals(BigDecimal.ZERO, report.getTotalCoinIn());
        assertEquals(BigDecimal.ZERO, report.getTotalCoinOut());
        assertEquals(BigDecimal.ZERO, report.getNetRevenue());
    }
}
