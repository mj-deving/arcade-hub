package com.mj.portfolio.dto;

import com.mj.portfolio.entity.Location;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Location details returned by the API")
public class LocationResponse {

    @Schema(description = "Unique location identifier")
    private UUID id;
    @Schema(description = "Location name", example = "Galaxy Spielhalle")
    private String name;
    @Schema(description = "Street address", example = "Reeperbahn 42, Hamburg")
    private String address;
    @Schema(description = "Maximum visitor capacity", example = "150")
    private int maxCapacity;
    @Schema(description = "Current number of visitors on premises", example = "73")
    private int currentOccupancy;
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    public static LocationResponse from(Location loc) {
        LocationResponse r = new LocationResponse();
        r.id = loc.getId();
        r.name = loc.getName();
        r.address = loc.getAddress();
        r.maxCapacity = loc.getMaxCapacity();
        r.currentOccupancy = loc.getCurrentOccupancy();
        r.createdAt = loc.getCreatedAt();
        r.updatedAt = loc.getUpdatedAt();
        return r;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public int getMaxCapacity() { return maxCapacity; }
    public int getCurrentOccupancy() { return currentOccupancy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
