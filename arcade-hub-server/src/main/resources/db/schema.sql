CREATE TABLE IF NOT EXISTS locations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200),
    max_capacity INTEGER NOT NULL DEFAULT 100,
    current_occupancy INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS arcade_machines (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'OFFLINE',
    location_id UUID REFERENCES locations(id) ON DELETE SET NULL,
    last_heartbeat TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS machine_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    machine_id UUID REFERENCES arcade_machines(id) ON DELETE SET NULL,
    event_type VARCHAR(50) NOT NULL,
    value NUMERIC(10, 2),
    timestamp TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS access_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    location_id UUID REFERENCES locations(id) ON DELETE SET NULL,
    person_id VARCHAR(100),
    event_type VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_arcade_machines_status      ON arcade_machines(status);
CREATE INDEX IF NOT EXISTS idx_arcade_machines_location_id ON arcade_machines(location_id);
CREATE INDEX IF NOT EXISTS idx_machine_events_machine_id   ON machine_events(machine_id);
CREATE INDEX IF NOT EXISTS idx_machine_events_timestamp    ON machine_events(timestamp);
CREATE INDEX IF NOT EXISTS idx_access_events_location_id   ON access_events(location_id);
CREATE INDEX IF NOT EXISTS idx_access_events_timestamp     ON access_events(timestamp);
