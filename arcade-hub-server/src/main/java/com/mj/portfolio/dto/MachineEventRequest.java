package com.mj.portfolio.dto;

import com.mj.portfolio.entity.enums.MachineEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Request payload for recording a machine telemetry event")
public class MachineEventRequest {

    @Schema(description = "Machine that generated the event")
    @NotNull(message = "Machine ID is required")
    private UUID machineId;

    @Schema(description = "Type of event", example = "COIN_IN")
    @NotNull(message = "Event type is required")
    private MachineEventType eventType;

    @Schema(description = "Monetary value (for COIN_IN/COIN_OUT events)", example = "2.50")
    private BigDecimal value;

    public UUID getMachineId() { return machineId; }
    public void setMachineId(UUID machineId) { this.machineId = machineId; }
    public MachineEventType getEventType() { return eventType; }
    public void setEventType(MachineEventType eventType) { this.eventType = eventType; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
}
