package com.mj.portfolio.websocket;

import com.mj.portfolio.dto.WebSocketEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Thin wrapper around SimpMessagingTemplate so services don't depend
 * directly on the WebSocket infrastructure. Also makes it trivially
 * mockable in @WebMvcTest slices (which don't load the WS broker beans).
 */
@Service
public class EventBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;

    public EventBroadcaster(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcast(WebSocketEvent event) {
        messagingTemplate.convertAndSend("/topic/events", event);
    }
}
