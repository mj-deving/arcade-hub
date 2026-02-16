package com.mj.portfolio.dto;

import com.mj.portfolio.entity.enums.AccessEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Request payload for recording a visitor access event")
public class AccessEventRequest {

    @Schema(description = "Location where the event occurred")
    @NotNull(message = "Location ID is required")
    private UUID locationId;

    @Schema(description = "Visitor identifier (Sperrdatei reference)", example = "DE-2026-00042")
    private String personId;

    @Schema(description = "Access event type", example = "CHECK_IN")
    @NotNull(message = "Event type is required")
    private AccessEventType eventType;

    public UUID getLocationId() { return locationId; }
    public void setLocationId(UUID locationId) { this.locationId = locationId; }
    public String getPersonId() { return personId; }
    public void setPersonId(String personId) { this.personId = personId; }
    public AccessEventType getEventType() { return eventType; }
    public void setEventType(AccessEventType eventType) { this.eventType = eventType; }
}
