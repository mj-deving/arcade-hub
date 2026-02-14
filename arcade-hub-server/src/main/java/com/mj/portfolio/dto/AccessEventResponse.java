package com.mj.portfolio.dto;

import com.mj.portfolio.entity.AccessEvent;
import com.mj.portfolio.entity.enums.AccessEventType;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccessEventResponse {

    private UUID id;
    private UUID locationId;
    private String locationName;
    private String personId;
    private AccessEventType eventType;
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
