package com.mj.portfolio.dto;

import com.mj.portfolio.entity.ArcadeMachine;
import com.mj.portfolio.entity.enums.MachineStatus;
import com.mj.portfolio.entity.enums.MachineType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Arcade machine details returned by the API")
public class ArcadeMachineResponse {

    @Schema(description = "Unique machine identifier")
    private UUID id;
    @Schema(description = "Machine display name", example = "Pac-Man Classic")
    private String name;
    @Schema(description = "Machine type", example = "SLOT")
    private MachineType type;
    @Schema(description = "Current operational status", example = "ONLINE")
    private MachineStatus status;
    @Schema(description = "ID of the assigned location")
    private UUID locationId;
    @Schema(description = "Name of the assigned location", example = "Galaxy Spielhalle")
    private String locationName;
    @Schema(description = "Last heartbeat received from this machine")
    private LocalDateTime lastHeartbeat;
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;
    @Schema(description = "Last update timestamp")
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
