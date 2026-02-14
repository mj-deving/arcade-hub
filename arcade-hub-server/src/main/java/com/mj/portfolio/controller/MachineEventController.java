package com.mj.portfolio.controller;

import com.mj.portfolio.dto.MachineEventRequest;
import com.mj.portfolio.dto.MachineEventResponse;
import com.mj.portfolio.service.MachineEventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/arcade/api/machine-events")
public class MachineEventController {

    private final MachineEventService service;

    public MachineEventController(MachineEventService service) {
        this.service = service;
    }

    @GetMapping
    public Page<MachineEventResponse> list(
            @RequestParam(required = false) UUID machineId,
            @PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(machineId, pageable);
    }

    @PostMapping
    public ResponseEntity<MachineEventResponse> create(
            @Valid @RequestBody MachineEventRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.record(req));
    }
}
