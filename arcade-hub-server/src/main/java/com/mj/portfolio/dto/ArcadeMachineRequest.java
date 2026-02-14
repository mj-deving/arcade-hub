package com.mj.portfolio.dto;

import com.mj.portfolio.entity.enums.MachineStatus;
import com.mj.portfolio.entity.enums.MachineType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ArcadeMachineRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Type is required")
    private MachineType type;

    private MachineStatus status;

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
