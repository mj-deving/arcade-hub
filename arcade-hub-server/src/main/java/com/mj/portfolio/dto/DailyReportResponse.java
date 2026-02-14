package com.mj.portfolio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class DailyReportResponse {

    private UUID locationId;
    private String locationName;
    private LocalDate date;
    private BigDecimal totalCoinIn;
    private BigDecimal totalCoinOut;
    private BigDecimal netRevenue;
    private long errorCount;
    private long maintenanceCount;
    private long checkInCount;
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
