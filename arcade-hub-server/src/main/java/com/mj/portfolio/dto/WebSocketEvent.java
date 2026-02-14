package com.mj.portfolio.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Payload broadcast over STOMP to /topic/events.
 * Intentionally flat so the browser can JSON.parse() it without nested object traversal.
 */
public class WebSocketEvent {

    private String type;
    private UUID machineId;
    private String machineName;
    private String eventType;
    private BigDecimal value;
    private LocalDateTime timestamp;
    private UUID locationId;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public UUID getMachineId() { return machineId; }
    public void setMachineId(UUID machineId) { this.machineId = machineId; }
    public String getMachineName() { return machineName; }
    public void setMachineName(String machineName) { this.machineName = machineName; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public UUID getLocationId() { return locationId; }
    public void setLocationId(UUID locationId) { this.locationId = locationId; }
}
