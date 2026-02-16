package com.mj.portfolio.controller;

import com.mj.portfolio.dto.MachineEventRequest;
import com.mj.portfolio.dto.MachineEventResponse;
import com.mj.portfolio.service.MachineEventService;
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

@Tag(name = "Machine Events", description = "Record and query machine telemetry events (coin-in, coin-out, errors)")
@RestController
@RequestMapping("/arcade/api/machine-events")
public class MachineEventController {

    private final MachineEventService service;

    public MachineEventController(MachineEventService service) {
        this.service = service;
    }

    @Operation(summary = "List machine events", description = "Paginated list, optionally filtered by machine ID")
    @GetMapping
    public Page<MachineEventResponse> list(
            @Parameter(description = "Filter events by machine") @RequestParam(required = false) UUID machineId,
            @PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(machineId, pageable);
    }

    @Operation(summary = "Record a machine event", description = "Creates an event and broadcasts it via WebSocket to /topic/events")
    @PostMapping
    public ResponseEntity<MachineEventResponse> create(
            @Valid @RequestBody MachineEventRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.record(req));
    }
}
