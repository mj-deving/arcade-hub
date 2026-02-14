package com.mj.portfolio.simulator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Map;

/**
 * HTTP client for the arcade-hub-server REST API.
 * Uses java.net.http.HttpClient (Java 11+) - no additional dependencies needed.
 */
public class ApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final String baseUrl;
    private final String authHeader;

    public ApiClient(SimulatorConfig config) {
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
        this.baseUrl = config.getApiUrl();
        String credentials = config.getApiUsername() + ":" + config.getApiPassword();
        this.authHeader = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    /**
     * Registers a new arcade machine and returns its assigned UUID string.
     */
    public String registerMachine(String name, String type) throws Exception {
        Map<String, Object> body = Map.of("name", name, "type", type);
        String json = mapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/arcade/api/machines"))
                .header("Content-Type", "application/json")
                .header("Authorization", authHeader)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 201) {
            throw new RuntimeException("Failed to register machine '" + name + "': HTTP " + response.statusCode());
        }
        JsonNode node = mapper.readTree(response.body());
        return node.get("id").asText();
    }

    /**
     * Sends a heartbeat PATCH to mark the machine as ONLINE.
     */
    public void sendHeartbeat(String machineId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/arcade/api/machines/" + machineId + "/heartbeat"))
                .header("Authorization", authHeader)
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Heartbeat failed for " + machineId + ": HTTP " + response.statusCode());
        }
    }

    /**
     * Posts a machine event (COIN_IN, COIN_OUT, ERROR, MAINTENANCE).
     */
    public void sendEvent(String machineId, String eventType, Double value) throws Exception {
        Map<String, Object> body = value != null
                ? Map.of("machineId", machineId, "eventType", eventType, "value", value)
                : Map.of("machineId", machineId, "eventType", eventType);

        String json = mapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/arcade/api/machine-events"))
                .header("Content-Type", "application/json")
                .header("Authorization", authHeader)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 201) {
            throw new RuntimeException("Event POST failed for " + machineId + ": HTTP " + response.statusCode());
        }
    }
}
