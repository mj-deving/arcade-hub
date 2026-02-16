package com.mj.portfolio.controller;

import com.mj.portfolio.dto.ArcadeMachineRequest;
import com.mj.portfolio.dto.ArcadeMachineResponse;
import com.mj.portfolio.entity.enums.MachineStatus;
import com.mj.portfolio.service.ArcadeMachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Arcade Machines", description = "CRUD and heartbeat operations for arcade machines")
@RestController
@RequestMapping("/arcade/api/machines")
public class ArcadeMachineController {

    private final ArcadeMachineService service;

    public ArcadeMachineController(ArcadeMachineService service) {
        this.service = service;
    }

    @Operation(summary = "List machines", description = "Paginated list with optional status filter")
    @GetMapping
    public Page<ArcadeMachineResponse> list(
            @Parameter(description = "Filter by machine status") @RequestParam(required = false) MachineStatus status,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return service.findAll(status, pageable);
    }

    @Operation(summary = "Get machine by ID")
    @GetMapping("/{id}")
    public ArcadeMachineResponse get(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Register a new machine")
    @PostMapping
    public ResponseEntity<ArcadeMachineResponse> create(
            @Valid @RequestBody ArcadeMachineRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @Operation(summary = "Update machine details")
    @PutMapping("/{id}")
    public ArcadeMachineResponse update(@PathVariable UUID id,
                                        @Valid @RequestBody ArcadeMachineRequest req) {
        return service.update(id, req);
    }

    @Operation(summary = "Send heartbeat", description = "Updates the machine's last-heartbeat timestamp and sets status to ONLINE")
    @PatchMapping("/{id}/heartbeat")
    public ArcadeMachineResponse heartbeat(@PathVariable UUID id) {
        return service.heartbeat(id);
    }

    @Operation(summary = "Delete a machine")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
