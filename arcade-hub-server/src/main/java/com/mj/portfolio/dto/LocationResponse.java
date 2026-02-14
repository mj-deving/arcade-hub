package com.mj.portfolio.dto;

import com.mj.portfolio.entity.Location;

import java.time.LocalDateTime;
import java.util.UUID;

public class LocationResponse {

    private UUID id;
    private String name;
    private String address;
    private int maxCapacity;
    private int currentOccupancy;
    private LocalDateTime createdAt;
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
