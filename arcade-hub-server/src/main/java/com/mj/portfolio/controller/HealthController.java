package com.mj.portfolio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Health", description = "Service health check")
@RestController
public class HealthController {

    @Operation(summary = "Health check", description = "Returns service status for monitoring and load balancer probes")
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "arcade-hub-server"));
    }
}
