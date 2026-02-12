# Arcade Hub

**The Showpiece Project** - Complete arcade machine management system with real-time monitoring, access control, and business analytics.

Directly addresses the job requirements: Gaming/arcade machine network management, status monitoring, event tracking, and multi-client architecture.

## Project 4 - Learning Goals (Advanced)

Demonstrate enterprise architecture skills:
- WebSocket real-time communication
- Multi-module complex architecture
- Event-driven simulation engine
- Advanced Spring Boot patterns
- Domain-driven design thinking
- Production-ready code
- Comprehensive documentation
- Live demo capability

## Tech Stack

**Server:**
- Spring Boot 3.2
- Spring WebSocket
- Spring Data JPA
- PostgreSQL
- Swagger/OpenAPI

**Simulator:**
- Java standalone application
- Scheduled tasks
- REST client

**Web:**
- HTML5, CSS3, Vanilla JavaScript
- Bootstrap 5
- WebSocket client
- Chart.js

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
GET    /api/machines              - List all machines
GET    /api/machines/{id}         - Machine details
POST   /api/machines              - Create machine
PUT    /api/machines/{id}         - Update machine
DELETE /api/machines/{id}         - Delete machine

GET    /api/locations             - List locations
POST   /api/locations             - Create location
GET    /api/locations/{id}/stats  - Location statistics

GET    /api/events                - Event history
GET    /api/reports/daily         - Daily reports

WS     /ws/events                 - WebSocket for live updates
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

Modern web dashboard with real-time updates.

**Views:**
- **Dashboard:** Overview, KPIs, alerts
- **Machines Grid:** Visual status of all machines with live updates
- **Locations:** Multi-location management and statistics
- **Access Control:** Current venue occupancy, check-in/out
- **Reports:** Daily/weekly analytics with charts
- **Settings:** Configuration and administration

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
├── arcade-hub-server/
│   ├── src/main/
│   │   ├── java/com/mj/portfolio/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   ├── controller/
│   │   │   ├── websocket/
│   │   │   ├── event/
│   │   │   └── config/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── schema.sql
│   └── src/test/
│
├── arcade-hub-simulator/
│   ├── src/main/java/com/mj/portfolio/
│   │   ├── simulator/
│   │   ├── client/
│   │   └── model/
│   └── src/main/resources/
│       └── simulator.properties
│
├── arcade-hub-web/
│   ├── index.html
│   ├── css/
│   ├── js/
│   │   ├── websocket-client.js
│   │   ├── api.js
│   │   ├── dashboard.js
│   │   └── charts.js
│   └── assets/
│
├── pom.xml (parent)
├── docker-compose.yml        # Local development stack
└── README.md
```

## Docker (Optional - for easy local setup)

```bash
docker-compose up -d

# Services start:
# - PostgreSQL: localhost:5432
# - Server: localhost:8080
# - Simulator: starts automatically
# - Web: localhost:3000 (via nginx)
```

## CI/CD Pipeline (GitLab)

Automated build, test, and deployment:

1. **Build Stage:** Compile all 3 modules
2. **Test Stage:** Unit tests, integration tests
3. **Coverage:** Generate JaCoCo reports
4. **Deploy Stage:** Auto-deploy to VPS (main branch only)

## Demonstration Mode

Run with demo flag to populate initial data:

```bash
java -jar arcade-hub-server-1.0.0.jar \
  --spring.profiles.active=demo
```

Creates:
- 10 pre-configured locations
- 50 simulated machines
- Sample historical data
- Simulator automatically starts

Perfect for interviews - immediate live data!

## Key Architectural Decisions

1. **WebSocket for Real-time Updates** - Necessary for live machine status
2. **Separate Simulator Module** - Demonstrates understanding of distributed systems
3. **Event-Driven Design** - Clean separation of concerns
4. **Multi-Module Maven** - Professional project structure
5. **Domain Entities** - Clear business logic modeling
6. **REST API + WebSocket** - Shows understanding of different communication patterns

## Next Steps

After this project:
- ✓ Advanced Spring Boot mastery
- ✓ Real-time web applications
- ✓ Distributed systems thinking
- ✓ Production-ready architecture
- ✓ Complete portfolio ready for interviews

## Interview Talking Points

- "Designed this to directly simulate the arcade/gaming focus of your company"
- "WebSocket architecture shows understanding of real-time systems"
- "Simulator demonstrates distributed system thinking"
- "Multi-module approach shows enterprise architecture skills"
- "Ready to demo live with realistic data"
