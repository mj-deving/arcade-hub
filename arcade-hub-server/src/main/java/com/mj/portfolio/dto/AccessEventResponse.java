package com.mj.portfolio.dto;

import com.mj.portfolio.entity.AccessEvent;
import com.mj.portfolio.entity.enums.AccessEventType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Access event details returned by the API")
public class AccessEventResponse {

    @Schema(description = "Event ID")
    private UUID id;
    @Schema(description = "Location where the event occurred")
    private UUID locationId;
    @Schema(description = "Location name", example = "Galaxy Spielhalle")
    private String locationName;
    @Schema(description = "Visitor identifier", example = "DE-2026-00042")
    private String personId;
    @Schema(description = "Access event type", example = "CHECK_IN")
    private AccessEventType eventType;
    @Schema(description = "When the event occurred")
    private LocalDateTime timestamp;

    public static AccessEventResponse from(AccessEvent e) {
        AccessEventResponse r = new AccessEventResponse();
        r.id = e.getId();
        if (e.getLocation() != null) {
            r.locationId = e.getLocation().getId();
            r.locationName = e.getLocation().getName();
        }
        r.personId = e.getPersonId();
        r.eventType = e.getEventType();
        r.timestamp = e.getTimestamp();
        return r;
    }

    public UUID getId() { return id; }
    public UUID getLocationId() { return locationId; }
    public String getLocationName() { return locationName; }
    public String getPersonId() { return personId; }
    public AccessEventType getEventType() { return eventType; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
