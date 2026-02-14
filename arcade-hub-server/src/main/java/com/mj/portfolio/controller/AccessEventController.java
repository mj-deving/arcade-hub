package com.mj.portfolio.controller;

import com.mj.portfolio.dto.AccessEventRequest;
import com.mj.portfolio.dto.AccessEventResponse;
import com.mj.portfolio.service.AccessControlService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/arcade/api/access-events")
public class AccessEventController {

    private final AccessControlService service;

    public AccessEventController(AccessControlService service) {
        this.service = service;
    }

    @GetMapping
    public Page<AccessEventResponse> list(
            @RequestParam(required = false) UUID locationId,
            @PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(locationId, pageable);
    }

    @PostMapping
    public ResponseEntity<AccessEventResponse> create(
            @Valid @RequestBody AccessEventRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.recordEvent(req));
    }
}
