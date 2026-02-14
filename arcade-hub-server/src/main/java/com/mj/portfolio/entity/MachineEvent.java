package com.mj.portfolio.entity;

import com.mj.portfolio.entity.enums.MachineEventType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "machine_events")
public class MachineEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private ArcadeMachine machine;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 50)
    private MachineEventType eventType;

    @Column(precision = 10, scale = 2)
    private BigDecimal value;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    void prePersist() {
        if (timestamp == null) timestamp = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public ArcadeMachine getMachine() { return machine; }
    public void setMachine(ArcadeMachine machine) { this.machine = machine; }
    public MachineEventType getEventType() { return eventType; }
    public void setEventType(MachineEventType eventType) { this.eventType = eventType; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
