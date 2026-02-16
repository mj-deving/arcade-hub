package com.mj.portfolio.controller;

import com.mj.portfolio.dto.AccessEventRequest;
import com.mj.portfolio.dto.AccessEventResponse;
import com.mj.portfolio.service.AccessControlService;
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

@Tag(name = "Access Events", description = "Visitor check-in/check-out tracking for regulatory compliance")
@RestController
@RequestMapping("/arcade/api/access-events")
public class AccessEventController {

    private final AccessControlService service;

    public AccessEventController(AccessControlService service) {
        this.service = service;
    }

    @Operation(summary = "List access events", description = "Paginated list, optionally filtered by location")
    @GetMapping
    public Page<AccessEventResponse> list(
            @Parameter(description = "Filter by location") @RequestParam(required = false) UUID locationId,
            @PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(locationId, pageable);
    }

    @Operation(summary = "Record an access event", description = "Records a visitor check-in or check-out and updates location occupancy")
    @PostMapping
    public ResponseEntity<AccessEventResponse> create(
            @Valid @RequestBody AccessEventRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.recordEvent(req));
    }
}
