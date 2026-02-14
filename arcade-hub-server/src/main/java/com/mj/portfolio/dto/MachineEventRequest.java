package com.mj.portfolio.dto;

import com.mj.portfolio.entity.enums.MachineEventType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class MachineEventRequest {

    @NotNull(message = "Machine ID is required")
    private UUID machineId;

    @NotNull(message = "Event type is required")
    private MachineEventType eventType;

    private BigDecimal value;

    public UUID getMachineId() { return machineId; }
    public void setMachineId(UUID machineId) { this.machineId = machineId; }
    public MachineEventType getEventType() { return eventType; }
    public void setEventType(MachineEventType eventType) { this.eventType = eventType; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
}
