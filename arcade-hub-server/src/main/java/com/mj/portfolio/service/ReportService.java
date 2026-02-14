package com.mj.portfolio.service;

import com.mj.portfolio.dto.DailyReportResponse;
import com.mj.portfolio.entity.Location;
import com.mj.portfolio.entity.enums.AccessEventType;
import com.mj.portfolio.entity.enums.MachineEventType;
import com.mj.portfolio.repository.AccessEventRepository;
import com.mj.portfolio.repository.MachineEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ReportService {

    private final MachineEventRepository machineEventRepo;
    private final AccessEventRepository accessEventRepo;
    private final LocationService locationService;

    public ReportService(MachineEventRepository machineEventRepo,
                          AccessEventRepository accessEventRepo,
                          LocationService locationService) {
        this.machineEventRepo = machineEventRepo;
        this.accessEventRepo = accessEventRepo;
        this.locationService = locationService;
    }

    public DailyReportResponse getDailyReport(UUID locationId, LocalDate date) {
        Location location = locationService.getOrThrow(locationId);

        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.plusDays(1).atStartOfDay();

        // SUM can return null when no rows match - use BigDecimal.ZERO as fallback
        BigDecimal coinIn = machineEventRepo.sumValueByLocationAndTypeAndTimeRange(
                locationId, MachineEventType.COIN_IN, from, to);
        BigDecimal coinOut = machineEventRepo.sumValueByLocationAndTypeAndTimeRange(
                locationId, MachineEventType.COIN_OUT, from, to);

        coinIn  = (coinIn  != null) ? coinIn  : BigDecimal.ZERO;
        coinOut = (coinOut != null) ? coinOut : BigDecimal.ZERO;

        DailyReportResponse report = new DailyReportResponse();
        report.setLocationId(locationId);
        report.setLocationName(location.getName());
        report.setDate(date);
        report.setTotalCoinIn(coinIn);
        report.setTotalCoinOut(coinOut);
        report.setNetRevenue(coinIn.subtract(coinOut));
        report.setErrorCount(machineEventRepo.countByLocationAndTypeAndTimeRange(
                locationId, MachineEventType.ERROR, from, to));
        report.setMaintenanceCount(machineEventRepo.countByLocationAndTypeAndTimeRange(
                locationId, MachineEventType.MAINTENANCE, from, to));
        report.setCheckInCount(accessEventRepo.countByLocationAndTypeAndTimeRange(
                locationId, AccessEventType.CHECK_IN, from, to));
        report.setCheckOutCount(accessEventRepo.countByLocationAndTypeAndTimeRange(
                locationId, AccessEventType.CHECK_OUT, from, to));
        return report;
    }
}
