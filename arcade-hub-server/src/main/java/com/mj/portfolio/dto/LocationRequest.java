package com.mj.portfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request payload for creating or updating an arcade hall location")
public class LocationRequest {

    @Schema(description = "Location name", example = "Galaxy Spielhalle")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Street address", example = "Reeperbahn 42, Hamburg")
    private String address;

    @Schema(description = "Maximum visitor capacity (regulatory limit)", example = "150")
    @Positive(message = "Max capacity must be positive")
    private int maxCapacity = 100;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }
}
