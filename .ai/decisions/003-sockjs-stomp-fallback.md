# ADR 003: SockJS + STOMP (Not Raw WebSocket)

## Context

The arcade-hub web dashboard needs real-time event delivery from the server. Options: raw WebSocket API, or STOMP protocol over SockJS.

## Decision

Use STOMP messaging protocol over SockJS transport.

## Reasons

- **SockJS fallback**: Automatically falls back to HTTP long-polling, streaming, or XHR when native WebSocket is blocked (corporate proxies, older browsers)
- **STOMP semantics**: Provides pub/sub with topics (`/topic/events`), subscriptions, and message framing — no manual protocol design needed
- **Spring integration**: `@EnableWebSocketMessageBroker` + `SimpMessagingTemplate` provides server-side broadcasting with one line of code
- **Client simplicity**: STOMP.js handles connection lifecycle, subscriptions, heartbeats, and reconnection

## Trade-offs

| Aspect | STOMP + SockJS | Raw WebSocket |
|--------|----------------|---------------|
| Browser support | Universal (fallback) | Modern only |
| Protocol overhead | STOMP framing (~50 bytes) | Minimal |
| Pub/sub | Built-in topics | Manual routing |
| Spring support | First-class | Lower-level API |
| Client library | STOMP.js (7KB) | None needed |

## Consequences

- nginx must be configured for WebSocket upgrade headers at `/arcade/ws/`
- SockJS endpoint URL (`/ws/events`) differs from the subscription topic (`/topic/events`)
- STOMP.js and SockJS are loaded from CDN (~15KB combined)
