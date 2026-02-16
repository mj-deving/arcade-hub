# ADR 001: In-Memory STOMP Broker (Not RabbitMQ)

## Context

Arcade Hub needs real-time event broadcasting from the server to connected browser clients. Spring provides two STOMP broker options: a simple in-memory broker, or an external message broker like RabbitMQ.

## Decision

Use Spring's built-in simple in-memory STOMP broker.

## Reasons

- **Single instance**: The server runs as one process on one VPS — no need for distributed pub/sub
- **Zero ops overhead**: No RabbitMQ to install, configure, monitor, or restart
- **Sufficient throughput**: The simulator generates ~10 events/second — well within in-memory broker limits
- **Portfolio scope**: Demonstrates the WebSocket pattern without infrastructure complexity

## Trade-offs

| Aspect | In-Memory | RabbitMQ |
|--------|-----------|----------|
| Setup complexity | None | Install + configure broker |
| Horizontal scaling | Not supported | Built-in clustering |
| Message persistence | No (fire-and-forget) | Yes (durable queues) |
| Throughput ceiling | ~1000 msg/s | ~50,000 msg/s |
| Monitoring | None | Management UI |

## Consequences

- If the server restarts, all WebSocket connections drop and clients must reconnect (SockJS auto-reconnect handles this)
- Cannot scale to multiple server instances without switching to an external broker
- Sufficient for the portfolio use case; migration to RabbitMQ would only require changing `WebSocketConfig.java` (the STOMP abstraction makes this a ~5-line change)
