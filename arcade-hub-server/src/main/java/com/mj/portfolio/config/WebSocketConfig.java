package com.mj.portfolio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Simple in-memory broker for /topic destinations (server -> subscribers)
        registry.enableSimpleBroker("/topic");
        // Prefix for client->server @MessageMapping methods (not used in this project,
        // but required to distinguish application traffic from broker traffic)
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Primary WebSocket endpoint with SockJS fallback for browsers
        // that cannot use native WebSockets (e.g. behind restrictive proxies)
        registry.addEndpoint("/ws/events")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
