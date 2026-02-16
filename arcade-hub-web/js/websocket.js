/**
 * websocket.js — STOMP over SockJS client for real-time event streaming.
 *
 * Connects to /arcade/ws/events (via nginx proxy → Spring Boot :8081).
 * Subscribes to /topic/events for broadcast machine events.
 * Provides onEvent(callback) for dashboard.js and machines.js to register handlers.
 *
 * Uses: STOMP.js 7.0.0 + SockJS 1.6.1 (loaded via CDN in HTML).
 */

const WS_ENDPOINT = '/arcade/ws/events';
const WS_TOPIC = '/topic/events';

let stompClient = null;
let reconnectDelay = 1000;
const MAX_RECONNECT_DELAY = 30000;
const listeners = [];

/**
 * Register a callback for incoming WebSocket events.
 * @param {function(object): void} callback - receives parsed event object
 */
function onEvent(callback) {
    listeners.push(callback);
}

/**
 * Broadcast an event to all registered listeners.
 * @param {object} event
 */
function notifyListeners(event) {
    listeners.forEach(fn => {
        try { fn(event); } catch (e) { console.error('WebSocket listener error:', e); }
    });
}

/**
 * Update the connection status indicator in the UI.
 * @param {boolean} connected
 */
function updateConnectionStatus(connected) {
    const dot = document.getElementById('ws-status-dot');
    const text = document.getElementById('ws-status-text');
    if (dot) {
        dot.className = `connection-dot ${connected ? 'connected' : 'disconnected'}`;
    }
    if (text) {
        text.textContent = connected ? 'Live' : 'Disconnected';
    }
}

/**
 * Connect to the STOMP broker via SockJS.
 * Auto-reconnects with exponential backoff on disconnect.
 */
function connectWebSocket() {
    const socket = new SockJS(WS_ENDPOINT);
    stompClient = new StompJs.Client({
        webSocketFactory: () => socket,
        reconnectDelay: 0, // we handle reconnect ourselves
        debug: () => {}, // silent in production
    });

    stompClient.onConnect = () => {
        console.log('[WS] Connected to', WS_ENDPOINT);
        reconnectDelay = 1000;
        updateConnectionStatus(true);

        stompClient.subscribe(WS_TOPIC, (message) => {
            try {
                const event = JSON.parse(message.body);
                notifyListeners(event);
            } catch (e) {
                console.error('[WS] Failed to parse message:', e);
            }
        });
    };

    stompClient.onStompError = (frame) => {
        console.error('[WS] STOMP error:', frame.headers['message']);
        updateConnectionStatus(false);
    };

    stompClient.onWebSocketClose = () => {
        console.log(`[WS] Disconnected. Reconnecting in ${reconnectDelay / 1000}s...`);
        updateConnectionStatus(false);
        setTimeout(() => {
            reconnectDelay = Math.min(reconnectDelay * 2, MAX_RECONNECT_DELAY);
            connectWebSocket();
        }, reconnectDelay);
    };

    stompClient.activate();
}

/**
 * Disconnect from the STOMP broker.
 */
function disconnectWebSocket() {
    if (stompClient) {
        stompClient.deactivate();
        stompClient = null;
    }
}
