# ADR 002: Simulator as Standalone JAR (Not Embedded)

## Context

The arcade simulator generates fake machine telemetry (heartbeats, coin events, errors). It could either be embedded in the server process or built as a separate module.

## Decision

Build the simulator as a standalone fat JAR in its own Maven module (`arcade-hub-simulator`).

## Reasons

- **Separation of concerns**: Simulator code (random generation, threading) doesn't belong in production server code
- **Independent lifecycle**: Can start/stop the simulator without restarting the server
- **Realistic architecture**: Mirrors a real deployment where machines are external devices sending HTTP to the API
- **No Spring dependency**: Simulator uses plain Java (java.util.Properties, HttpURLConnection) — demonstrates non-Spring Java skills
- **Docker-friendly**: Can be containerized independently, with different resource limits

## Trade-offs

| Aspect | Standalone | Embedded |
|--------|-----------|----------|
| Deployment | Separate process | Toggle with config flag |
| Network | HTTP over loopback | Direct method calls |
| Configuration | simulator.properties | application.yml |
| Spring dependency | None | Full Spring context |

## Consequences

- Simulator exercises the full HTTP stack (auth, validation, persistence, WebSocket broadcast)
- Requires machines to be created via API before the simulator can run
- Network latency is negligible on localhost
