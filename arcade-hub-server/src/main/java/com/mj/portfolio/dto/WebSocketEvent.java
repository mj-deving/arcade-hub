package com.mj.portfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Payload broadcast over STOMP to /topic/events.
 * Intentionally flat so the browser can JSON.parse() it without nested object traversal.
 */
@Schema(description = "Real-time event broadcast via WebSocket to /topic/events")
public class WebSocketEvent {

    @Schema(description = "Event category", example = "MACHINE_EVENT")
    private String type;
    @Schema(description = "Machine that generated the event")
    private UUID machineId;
    @Schema(description = "Machine name", example = "Pac-Man Classic")
    private String machineName;
    @Schema(description = "Specific event type", example = "COIN_IN")
    private String eventType;
    @Schema(description = "Monetary value", example = "2.50")
    private BigDecimal value;
    @Schema(description = "When the event occurred")
    private LocalDateTime timestamp;
    @Schema(description = "Location of the machine")
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
