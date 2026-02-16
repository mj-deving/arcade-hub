package com.mj.portfolio.dto;

import com.mj.portfolio.entity.MachineEvent;
import com.mj.portfolio.entity.enums.MachineEventType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Machine event details returned by the API")
public class MachineEventResponse {

    @Schema(description = "Event ID")
    private UUID id;
    @Schema(description = "Machine that generated the event")
    private UUID machineId;
    @Schema(description = "Machine name", example = "Pac-Man Classic")
    private String machineName;
    @Schema(description = "Event type", example = "COIN_IN")
    private MachineEventType eventType;
    @Schema(description = "Monetary value", example = "2.50")
    private BigDecimal value;
    @Schema(description = "When the event occurred")
    private LocalDateTime timestamp;

    public static MachineEventResponse from(MachineEvent e) {
        MachineEventResponse r = new MachineEventResponse();
        r.id = e.getId();
        if (e.getMachine() != null) {
            r.machineId = e.getMachine().getId();
            r.machineName = e.getMachine().getName();
        }
        r.eventType = e.getEventType();
        r.value = e.getValue();
        r.timestamp = e.getTimestamp();
        return r;
    }

    public UUID getId() { return id; }
    public UUID getMachineId() { return machineId; }
    public String getMachineName() { return machineName; }
    public MachineEventType getEventType() { return eventType; }
    public BigDecimal getValue() { return value; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
