package com.mj.portfolio.simulator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Entry point for the arcade-hub standalone simulator.
 *
 * Starts N virtual machine threads, each independently registering with
 * the arcade-hub-server and sending events/heartbeats. Responds cleanly
 * to Ctrl+C (SIGINT) via a JVM shutdown hook.
 *
 * Usage:
 *   java -jar arcade-hub-simulator-1.0.0-jar-with-dependencies.jar
 *
 * Configuration: arcade-hub-simulator/src/main/resources/simulator.properties
 */
public class ArcadeSimulatorApp {

    public static void main(String[] args) throws InterruptedException {
        SimulatorConfig config = new SimulatorConfig();
        ApiClient apiClient = new ApiClient(config);
        EventGenerator eventGenerator = new EventGenerator();

        int machineCount = config.getMachineCount();
        CountDownLatch shutdownLatch = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(
                machineCount,
                r -> {
                    Thread t = new Thread(r);
                    t.setDaemon(false);
                    return t;
                });

        // Graceful shutdown on Ctrl+C or SIGTERM
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n[Simulator] Shutdown requested - stopping all machines...");
            shutdownLatch.countDown();
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            System.out.println("[Simulator] All machines stopped.");
        }));

        System.out.println("[Simulator] Starting " + machineCount + " virtual machines...");
        System.out.println("[Simulator] Server: " + config.getApiUrl());

        for (int i = 1; i <= machineCount; i++) {
            executor.submit(new VirtualMachine(i, config, apiClient, eventGenerator, shutdownLatch));
        }

        System.out.println("[Simulator] All machines started. Press Ctrl+C to stop.");

        // Keep main thread alive until shutdown is triggered
        shutdownLatch.await();
    }
}
