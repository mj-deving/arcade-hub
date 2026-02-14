package com.mj.portfolio.dto;

import com.mj.portfolio.entity.enums.AccessEventType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AccessEventRequest {

    @NotNull(message = "Location ID is required")
    private UUID locationId;

    private String personId;

    @NotNull(message = "Event type is required")
    private AccessEventType eventType;

    public UUID getLocationId() { return locationId; }
    public void setLocationId(UUID locationId) { this.locationId = locationId; }
    public String getPersonId() { return personId; }
    public void setPersonId(String personId) { this.personId = personId; }
    public AccessEventType getEventType() { return eventType; }
    public void setEventType(AccessEventType eventType) { this.eventType = eventType; }
}
