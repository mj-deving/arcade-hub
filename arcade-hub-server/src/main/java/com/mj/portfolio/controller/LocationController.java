package com.mj.portfolio.controller;

import com.mj.portfolio.dto.LocationRequest;
import com.mj.portfolio.dto.LocationResponse;
import com.mj.portfolio.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Locations", description = "Manage arcade hall locations and capacity")
@RestController
@RequestMapping("/arcade/api/locations")
public class LocationController {

    private final LocationService service;

    public LocationController(LocationService service) {
        this.service = service;
    }

    @Operation(summary = "List locations")
    @GetMapping
    public Page<LocationResponse> list(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return service.findAll(pageable);
    }

    @Operation(summary = "Get location by ID")
    @GetMapping("/{id}")
    public LocationResponse get(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Create a new location")
    @PostMapping
    public ResponseEntity<LocationResponse> create(@Valid @RequestBody LocationRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @Operation(summary = "Update location details")
    @PutMapping("/{id}")
    public LocationResponse update(@PathVariable UUID id,
                                   @Valid @RequestBody LocationRequest req) {
        return service.update(id, req);
    }

    @Operation(summary = "Delete a location")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
