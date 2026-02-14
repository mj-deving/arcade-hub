package com.mj.portfolio.repository;

import com.mj.portfolio.entity.MachineEvent;
import com.mj.portfolio.entity.enums.MachineEventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface MachineEventRepository extends JpaRepository<MachineEvent, UUID> {

    Page<MachineEvent> findByMachineId(UUID machineId, Pageable pageable);

    /**
     * Sum event values for a given location, event type, and time range.
     * Returns null when no rows match - callers must handle null (use BigDecimal.ZERO fallback).
     */
    @Query("""
            SELECT SUM(e.value) FROM MachineEvent e
            WHERE e.machine.location.id = :locationId
              AND e.eventType = :eventType
              AND e.timestamp BETWEEN :from AND :to
            """)
    BigDecimal sumValueByLocationAndTypeAndTimeRange(
            @Param("locationId") UUID locationId,
            @Param("eventType") MachineEventType eventType,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    /**
     * Count events for a given location, event type, and time range.
     */
    @Query("""
            SELECT COUNT(e) FROM MachineEvent e
            WHERE e.machine.location.id = :locationId
              AND e.eventType = :eventType
              AND e.timestamp BETWEEN :from AND :to
            """)
    long countByLocationAndTypeAndTimeRange(
            @Param("locationId") UUID locationId,
            @Param("eventType") MachineEventType eventType,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
