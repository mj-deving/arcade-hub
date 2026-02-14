package com.mj.portfolio.controller;

import com.mj.portfolio.dto.ArcadeMachineRequest;
import com.mj.portfolio.dto.ArcadeMachineResponse;
import com.mj.portfolio.entity.enums.MachineStatus;
import com.mj.portfolio.service.ArcadeMachineService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/arcade/api/machines")
public class ArcadeMachineController {

    private final ArcadeMachineService service;

    public ArcadeMachineController(ArcadeMachineService service) {
        this.service = service;
    }

    @GetMapping
    public Page<ArcadeMachineResponse> list(
            @RequestParam(required = false) MachineStatus status,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return service.findAll(status, pageable);
    }

    @GetMapping("/{id}")
    public ArcadeMachineResponse get(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<ArcadeMachineResponse> create(
            @Valid @RequestBody ArcadeMachineRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @PutMapping("/{id}")
    public ArcadeMachineResponse update(@PathVariable UUID id,
                                        @Valid @RequestBody ArcadeMachineRequest req) {
        return service.update(id, req);
    }

    @PatchMapping("/{id}/heartbeat")
    public ArcadeMachineResponse heartbeat(@PathVariable UUID id) {
        return service.heartbeat(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
