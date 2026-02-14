package com.mj.portfolio.simulator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Simulates one arcade machine. Runs as a standalone Runnable in its own thread.
 * - Registers with the server on startup
 * - Sends heartbeats at a configurable interval
 * - Generates events at a configurable interval
 *
 * Uses CountDownLatch for shutdown coordination: latch.await(500ms) wakes
 * immediately when latch reaches 0 (shutdown signal), rather than sleeping
 * for up to 500ms and then checking a flag.
 */
public class VirtualMachine implements Runnable {

    private final int index;
    private final SimulatorConfig config;
    private final ApiClient apiClient;
    private final EventGenerator eventGenerator;
    private final CountDownLatch shutdownLatch;

    private String machineId;

    public VirtualMachine(int index, SimulatorConfig config, ApiClient apiClient,
                           EventGenerator eventGenerator, CountDownLatch shutdownLatch) {
        this.index = index;
        this.config = config;
        this.apiClient = apiClient;
        this.eventGenerator = eventGenerator;
        this.shutdownLatch = shutdownLatch;
    }

    @Override
    public void run() {
        String name = "Sim-Machine-" + index;
        try {
            // Register with the server
            String type = pickType();
            machineId = apiClient.registerMachine(name, type);
            log("Registered with ID " + machineId + " (type=" + type + ")");
        } catch (Exception e) {
            log("Registration failed: " + e.getMessage() + " - exiting");
            return;
        }

        long lastHeartbeat = 0;
        long lastEvent = 0;
        long heartbeatIntervalMs = config.getHeartbeatIntervalSeconds() * 1000L;
        long eventIntervalMs = config.getEventIntervalSeconds() * 1000L;

        while (shutdownLatch.getCount() > 0) {
            long now = System.currentTimeMillis();

            if (now - lastHeartbeat >= heartbeatIntervalMs) {
                try {
                    apiClient.sendHeartbeat(machineId);
                    log("Heartbeat sent");
                    lastHeartbeat = now;
                } catch (Exception e) {
                    log("Heartbeat error: " + e.getMessage());
                }
            }

            if (now - lastEvent >= eventIntervalMs) {
                try {
                    EventGenerator.SimEvent event = eventGenerator.generate(config.getErrorRate());
                    apiClient.sendEvent(machineId, event.type(), event.value());
                    log("Event: " + event.type() + (event.value() != null ? " value=" + event.value() : ""));
                    lastEvent = now;
                } catch (Exception e) {
                    log("Event error: " + e.getMessage());
                }
            }

            try {
                // Wait up to 500ms — wakes immediately if shutdownLatch reaches 0
                shutdownLatch.await(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log("Shutdown complete");
    }

    private String pickType() {
        String[] types = {"SLOT_A", "SLOT_B", "POKER"};
        return types[index % types.length];
    }

    private void log(String message) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + message);
    }
}
