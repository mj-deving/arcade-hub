package com.mj.portfolio.simulator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads simulator.properties from the classpath and exposes typed getters.
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

    public String getApiUrl() {
        return props.getProperty("api.url", "http://localhost:8081");
    }

    public String getApiUsername() {
        return props.getProperty("api.username", "admin");
    }

    public String getApiPassword() {
        return props.getProperty("api.password", "admin123");
    }

    public int getMachineCount() {
        return Integer.parseInt(props.getProperty("machine.count", "10"));
    }

    public int getHeartbeatIntervalSeconds() {
        return Integer.parseInt(props.getProperty("heartbeat.interval.seconds", "30"));
    }

    public int getEventIntervalSeconds() {
        return Integer.parseInt(props.getProperty("event.interval.seconds", "5"));
    }

    public double getErrorRate() {
        return Double.parseDouble(props.getProperty("error.rate", "0.05"));
    }
}
