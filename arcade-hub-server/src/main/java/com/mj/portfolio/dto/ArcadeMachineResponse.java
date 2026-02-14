package com.mj.portfolio.dto;

import com.mj.portfolio.entity.ArcadeMachine;
import com.mj.portfolio.entity.enums.MachineStatus;
import com.mj.portfolio.entity.enums.MachineType;

import java.time.LocalDateTime;
import java.util.UUID;

public class ArcadeMachineResponse {

    private UUID id;
    private String name;
    private MachineType type;
    private MachineStatus status;
    private UUID locationId;
    private String locationName;
    private LocalDateTime lastHeartbeat;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ArcadeMachineResponse from(ArcadeMachine m) {
        ArcadeMachineResponse r = new ArcadeMachineResponse();
        r.id = m.getId();
        r.name = m.getName();
        r.type = m.getType();
        r.status = m.getStatus();
        if (m.getLocation() != null) {
            r.locationId = m.getLocation().getId();
            r.locationName = m.getLocation().getName();
        }
        r.lastHeartbeat = m.getLastHeartbeat();
        r.createdAt = m.getCreatedAt();
        r.updatedAt = m.getUpdatedAt();
        return r;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public MachineType getType() { return type; }
    public MachineStatus getStatus() { return status; }
    public UUID getLocationId() { return locationId; }
    public String getLocationName() { return locationName; }
    public LocalDateTime getLastHeartbeat() { return lastHeartbeat; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
