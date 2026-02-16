package com.mj.portfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Aggregated daily report for a location (revenue, errors, visitors)")
public class DailyReportResponse {

    @Schema(description = "Location ID")
    private UUID locationId;
    @Schema(description = "Location name", example = "Galaxy Spielhalle")
    private String locationName;
    @Schema(description = "Report date", example = "2026-02-16")
    private LocalDate date;
    @Schema(description = "Total coins inserted", example = "1250.00")
    private BigDecimal totalCoinIn;
    @Schema(description = "Total coins paid out", example = "875.00")
    private BigDecimal totalCoinOut;
    @Schema(description = "Net revenue (coinIn - coinOut)", example = "375.00")
    private BigDecimal netRevenue;
    @Schema(description = "Number of machine errors", example = "3")
    private long errorCount;
    @Schema(description = "Number of maintenance events", example = "1")
    private long maintenanceCount;
    @Schema(description = "Number of visitor check-ins", example = "147")
    private long checkInCount;
    @Schema(description = "Number of visitor check-outs", example = "142")
    private long checkOutCount;

    public UUID getLocationId() { return locationId; }
    public void setLocationId(UUID locationId) { this.locationId = locationId; }
    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public BigDecimal getTotalCoinIn() { return totalCoinIn; }
    public void setTotalCoinIn(BigDecimal totalCoinIn) { this.totalCoinIn = totalCoinIn; }
    public BigDecimal getTotalCoinOut() { return totalCoinOut; }
    public void setTotalCoinOut(BigDecimal totalCoinOut) { this.totalCoinOut = totalCoinOut; }
    public BigDecimal getNetRevenue() { return netRevenue; }
    public void setNetRevenue(BigDecimal netRevenue) { this.netRevenue = netRevenue; }
    public long getErrorCount() { return errorCount; }
    public void setErrorCount(long errorCount) { this.errorCount = errorCount; }
    public long getMaintenanceCount() { return maintenanceCount; }
    public void setMaintenanceCount(long maintenanceCount) { this.maintenanceCount = maintenanceCount; }
    public long getCheckInCount() { return checkInCount; }
    public void setCheckInCount(long checkInCount) { this.checkInCount = checkInCount; }
    public long getCheckOutCount() { return checkOutCount; }
    public void setCheckOutCount(long checkOutCount) { this.checkOutCount = checkOutCount; }
}
