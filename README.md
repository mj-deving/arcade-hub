# Arcade Hub

[![CI](https://github.com/mj-deving/arcade-hub/actions/workflows/ci.yml/badge.svg)](https://github.com/mj-deving/arcade-hub/actions/workflows/ci.yml)
![Java 17](https://img.shields.io/badge/Java-17-blue?logo=openjdk)
![Spring Boot 3.2](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen?logo=springboot)
![PostgreSQL 16](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)

**The Showpiece Project** — Complete arcade machine management system with real-time monitoring, access control, and business analytics.

Directly addresses gaming industry requirements: machine network management, status monitoring, event tracking, regulatory compliance (GlüStV 2021), and multi-client architecture.

Live API: `http://213.199.32.18/arcade/api/machines` (Basic Auth required)
Live Dashboard: `http://213.199.32.18/arcade/` (login: admin / admin123)

## Tech Stack

**Server (Spring Boot 3.2):** REST API, WebSocket (STOMP), JPA, PostgreSQL, Swagger/OpenAPI, JaCoCo coverage

**Simulator (Standalone Java):** Multi-threaded event generator, configurable via properties or env vars

**Web Dashboard (HTML/JS):** Bootstrap 5, Chart.js 4, STOMP.js + SockJS for real-time updates

## Modules

### arcade-hub-server

Central management server for arcade operations.

**Core Entities:**
- `ArcadeMachine` - Individual game machine with status tracking
- `Location` - Physical arcade location/venue
- `AccessEvent` - Entry/exit events for access control
- `MachineEvent` - Device-level events (coin in/out, errors)

**Key Features:**
- REST API for all operations
- WebSocket endpoint for live updates
- Simulation event generation
- Capacity management
- Event aggregation and reporting

**API Endpoints:**
```
GET    /arcade/api/machines              - List machines (paginated, filterable)
GET    /arcade/api/machines/{id}         - Machine details
POST   /arcade/api/machines              - Register machine
PUT    /arcade/api/machines/{id}         - Update machine
PATCH  /arcade/api/machines/{id}/heartbeat - Send heartbeat
DELETE /arcade/api/machines/{id}         - Delete machine

GET    /arcade/api/locations             - List locations
POST   /arcade/api/locations             - Create location
PUT    /arcade/api/locations/{id}        - Update location
DELETE /arcade/api/locations/{id}        - Delete location

GET    /arcade/api/machine-events        - Event history (filterable by machineId)
POST   /arcade/api/machine-events        - Record event (broadcasts via WebSocket)

GET    /arcade/api/access-events         - Access history (filterable by locationId)
POST   /arcade/api/access-events         - Record check-in/check-out

GET    /arcade/api/reports/daily          - Daily report (locationId + date)

WS     /arcade/ws/events                  - STOMP/SockJS for live updates
       Subscribe: /topic/events

GET    /health                            - Service health check
GET    /swagger-ui.html                   - Interactive API documentation
```

### arcade-hub-simulator

Standalone Java application simulating realistic arcade operations.

**Simulates:**
- Periodic heartbeats from machines
- Random coin transactions
- Occasional machine errors
- Access events (people entering/leaving)
- Network latency and failures

**Features:**
- Configurable machine count
- Variable event frequencies
- Realistic error rates
- Easy to start/stop

```bash
java -jar arcade-hub-simulator-1.0.0.jar \
  --machines=50 \
  --event-frequency=5000 \
  --error-rate=0.05
```

### arcade-hub-web

Real-time monitoring dashboard with WebSocket integration.

**Pages:**
- **Login** (`index.html`): Basic Auth login, validates against live API
- **Dashboard** (`dashboard.html`): Stat cards (Total/Online/Maintenance/Error), doughnut chart by type, live event feed via WebSocket
- **Machines** (`machines.html`): Full machine table with status filter, live heartbeat freshness indicators (green < 60s, amber < 300s, red > 300s), real-time status updates via WebSocket

**Tech:** Bootstrap 5, Chart.js 4, STOMP.js 7.0 + SockJS 1.6 (CDN), amber/gold accent theme

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL 16
- Modern web browser

### Full Stack (Local Development)

**Terminal 1 - Server:**
```bash
cd arcade-hub-server
mvn spring-boot:run
```

**Terminal 2 - Simulator:**
```bash
cd arcade-hub-simulator
mvn exec:java -Dexec.mainClass="com.mj.portfolio.simulator.ArcadeSimulator"
```

**Terminal 3 - Web Dashboard:**
```bash
cd arcade-hub-web
python3 -m http.server 3000
# Open http://localhost:3000
```

### Production Deployment (VPS)

```bash
# Build all modules
mvn clean package

# Deploy server
/home/dev/deploy-app.sh arcade-hub target/arcade-hub-server-1.0.0.jar 8080

# Deploy simulator as background service
nohup java -jar target/arcade-hub-simulator-1.0.0.jar &

# Deploy web (copy to nginx)
cp arcade-hub-web/* /var/www/portfolio/
```

## Project Structure

```
arcade-hub/
├── pom.xml                        ← parent POM (spring-boot-dependencies BOM)
├── .github/workflows/ci.yml      ← GitHub Actions CI + deploy
├── deploy-web.sh                  ← SCP web files to VPS
├── LEARNING.md                    ← Domain context (GlüStV 2021)
│
├── arcade-hub-server/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/mj/portfolio/
│       ├── config/                ← Security, CORS, WebSocket, OpenAPI
│       ├── controller/            ← 6 REST controllers
│       ├── dto/                   ← Request/Response DTOs with @Schema
│       ├── entity/                ← JPA entities + enums
│       ├── repository/            ← Spring Data JPA interfaces
│       ├── service/               ← Business logic + event broadcasting
│       └── resources/application.yml
│
├── arcade-hub-simulator/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/mj/portfolio/simulator/
│       ├── ArcadeSimulatorApp.java   ← Entry point
│       ├── SimulatorConfig.java       ← Properties + env var overrides
│       ├── ApiClient.java             ← HTTP client with Basic Auth
│       ├── EventGenerator.java        ← Random event generation
│       └── VirtualMachine.java        ← Thread per machine
│
├── arcade-hub-web/
│   ├── index.html                 ← Login page
│   ├── dashboard.html             ← Real-time dashboard
│   ├── machines.html              ← Machine list with live updates
│   ├── css/app.css                ← Amber/gold themed styles
│   └── js/
│       ├── auth.js                ← Basic Auth management
│       ├── api.js                 ← REST API wrapper
│       ├── utils.js               ← Status badges, formatting
│       ├── websocket.js           ← STOMP.js + SockJS client
│       ├── dashboard.js           ← Charts + live event feed
│       └── machines.js            ← Machine table + live updates
│
└── .ai/decisions/                 ← Architecture Decision Records
    ├── 001-in-memory-stomp-broker.md
    ├── 002-simulator-standalone-jar.md
    └── 003-sockjs-stomp-fallback.md
```

## CI/CD Pipeline (GitHub Actions)

Automated build, test, and deployment:

1. **Build:** Compile all modules
2. **Test:** Server unit tests with **JaCoCo coverage** (uploaded as artifact)
3. **Package:** Fat JARs for server + simulator
4. **Deploy:** SSH to VPS, pull + build + restart (master only, manual approval via GitHub Environments)

Requires `VPS_SSH_KEY` secret and `production` environment configured in GitHub.

## Docker Quick Start

See [DOCKER.md](../DOCKER.md) at the portfolio root for full instructions:

```bash
# Build JARs first, then:
docker-compose up --build   # from portfolio root
# Visit http://localhost/arcade/
```

## Key Architectural Decisions

| Decision | Rationale | ADR |
|----------|-----------|-----|
| In-memory STOMP broker | Single instance, zero ops overhead, sufficient for portfolio | [001](.ai/decisions/001-in-memory-stomp-broker.md) |
| Simulator as standalone JAR | Separation of concerns, realistic architecture, no Spring dependency | [002](.ai/decisions/002-simulator-standalone-jar.md) |
| SockJS + STOMP | Universal browser support, built-in pub/sub, Spring first-class support | [003](.ai/decisions/003-sockjs-stomp-fallback.md) |
| Multi-module Maven | Clean module boundaries, independent build/deploy lifecycle | — |
| Domain model maps to GlüStV 2021 | Locations (capacity), access events (Sperrdatei), machine events (revenue audit) | [LEARNING.md](LEARNING.md) |

## Documentation

- **[LEARNING.md](LEARNING.md)** — German arcade regulation context and how the data model maps to it
- **[.ai/decisions/](.ai/decisions/)** — Architecture Decision Records
- **[.ai/diagrams/](../.ai/diagrams/)** — C4 and sequence diagrams (Mermaid, renders on GitHub)
- **Swagger UI** — Interactive API docs at `/swagger-ui.html`

---

*Part of a 4-project portfolio demonstrating Java full-stack development from CLI → REST API → Web UI → WebSocket.*
