package com.mj.portfolio.dto;

import com.mj.portfolio.entity.enums.MachineStatus;
import com.mj.portfolio.entity.enums.MachineType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Request payload for creating or updating an arcade machine")
public class ArcadeMachineRequest {

    @Schema(description = "Machine display name", example = "Pac-Man Classic")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Machine type", example = "SLOT")
    @NotNull(message = "Type is required")
    private MachineType type;

    @Schema(description = "Initial status (defaults to OFFLINE if not specified)")
    private MachineStatus status;

    @Schema(description = "Location where this machine is installed")
    private UUID locationId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public MachineType getType() { return type; }
    public void setType(MachineType type) { this.type = type; }
    public MachineStatus getStatus() { return status; }
    public void setStatus(MachineStatus status) { this.status = status; }
    public UUID getLocationId() { return locationId; }
    public void setLocationId(UUID locationId) { this.locationId = locationId; }
}
