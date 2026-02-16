# Arcade Hub — Domain Context & Learning

## German Arcade Hall Regulation (Spielhallenrecht)

The **GlüStV 2021** (Glücksspielstaatsvertrag) is Germany's interstate gambling treaty. It imposes strict requirements on arcade halls (Spielhallen):

- **Occupancy limits**: Each location has a maximum visitor capacity. Operators must track check-ins and check-outs to ensure compliance.
- **Sperrdatei** (exclusion database): A centralized system where self-excluded gamblers are registered. Halls must verify visitors against this database before entry.
- **Revenue auditing**: All coin-in and coin-out transactions must be logged for tax reporting and regulatory review.
- **Machine monitoring**: Operators must track machine status, errors, and maintenance to ensure gaming devices meet technical standards (TÜV certification).

## How the Data Model Maps to Regulations

| Regulation | Data Model | Implementation |
|-----------|------------|----------------|
| Occupancy limits | `locations.max_capacity`, `locations.current_occupancy` | `AccessControlService` increments/decrements on CHECK_IN/CHECK_OUT |
| Sperrdatei | `access_events.person_id` | Visitor ID recorded at check-in (would integrate with external Sperrdatei API in production) |
| Revenue auditing | `machine_events` (COIN_IN, COIN_OUT) | `ReportService.getDailyReport()` aggregates by location and date |
| Machine monitoring | `arcade_machines.status`, `arcade_machines.last_heartbeat` | Heartbeat endpoint + WebSocket broadcast for real-time dashboard |

## Technical Patterns Demonstrated

### 1. Event-Driven Architecture

Events flow through the system as first-class data:

```
Simulator → REST API → Database + WebSocket Broadcast → Browser Dashboard
```

Each machine event is both persisted (for reporting) and broadcast (for real-time display). This dual-write pattern is common in monitoring systems.

### 2. WebSocket with STOMP

The real-time layer uses STOMP (Simple Text Oriented Messaging Protocol) over SockJS:

- **Server**: `SimpMessagingTemplate.convertAndSend("/topic/events", event)`
- **Client**: `stompClient.subscribe("/topic/events", callback)`
- **Fallback**: SockJS automatically downgrades to HTTP long-polling if WebSocket is blocked

### 3. Simulator as External Consumer

The simulator is a standalone Java process that consumes the same REST API as the web dashboard. This validates the API design from a client perspective and generates realistic test data without modifying server code.

## Architecture Decisions

See `.ai/decisions/` for detailed ADRs:
- [001: In-Memory STOMP Broker](/.ai/decisions/001-in-memory-stomp-broker.md)
- [002: Simulator as Standalone JAR](/.ai/decisions/002-simulator-standalone-jar.md)
- [003: SockJS + STOMP Fallback](/.ai/decisions/003-sockjs-stomp-fallback.md)
