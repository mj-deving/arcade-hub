package com.mj.portfolio.simulator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads simulator.properties from the classpath and exposes typed getters.
 * Environment variables override properties file values (for Docker).
 * Using java.util.Properties keeps the simulator free of any Spring dependency.
 */
public class SimulatorConfig {

    private final Properties props = new Properties();

    public SimulatorConfig() {
        try (InputStream is = SimulatorConfig.class.getClassLoader()
                .getResourceAsStream("simulator.properties")) {
            if (is == null) {
                throw new RuntimeException("simulator.properties not found on classpath");
            }
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load simulator.properties", e);
        }
    }

    /**
     * Check environment variable first, fall back to properties file, then default.
     */
    private String resolve(String envVar, String propKey, String defaultValue) {
        String env = System.getenv(envVar);
        if (env != null && !env.isBlank()) return env;
        return props.getProperty(propKey, defaultValue);
    }

    public String getApiUrl() {
        return resolve("API_URL", "api.url", "http://localhost:8081");
    }

    public String getApiUsername() {
        return resolve("API_USERNAME", "api.username", "admin");
    }

    public String getApiPassword() {
        return resolve("API_PASSWORD", "api.password", "admin123");
    }

    public int getMachineCount() {
        return Integer.parseInt(resolve("MACHINE_COUNT", "machine.count", "10"));
    }

    public int getHeartbeatIntervalSeconds() {
        return Integer.parseInt(resolve("HEARTBEAT_INTERVAL", "heartbeat.interval.seconds", "30"));
    }

    public int getEventIntervalSeconds() {
        return Integer.parseInt(resolve("EVENT_INTERVAL", "event.interval.seconds", "5"));
    }

    public double getErrorRate() {
        return Double.parseDouble(resolve("ERROR_RATE", "error.rate", "0.05"));
    }
}
