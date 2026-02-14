package com.mj.portfolio.dto;

import com.mj.portfolio.entity.MachineEvent;
import com.mj.portfolio.entity.enums.MachineEventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class MachineEventResponse {

    private UUID id;
    private UUID machineId;
    private String machineName;
    private MachineEventType eventType;
    private BigDecimal value;
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
