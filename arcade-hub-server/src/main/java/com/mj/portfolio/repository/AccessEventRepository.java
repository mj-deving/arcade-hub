package com.mj.portfolio.repository;

import com.mj.portfolio.entity.AccessEvent;
import com.mj.portfolio.entity.enums.AccessEventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AccessEventRepository extends JpaRepository<AccessEvent, UUID> {

    Page<AccessEvent> findByLocationId(UUID locationId, Pageable pageable);

    /**
     * Count access events for a given location, event type, and time range.
     */
    @Query("""
            SELECT COUNT(e) FROM AccessEvent e
            WHERE e.location.id = :locationId
              AND e.eventType = :eventType
              AND e.timestamp BETWEEN :from AND :to
            """)
    long countByLocationAndTypeAndTimeRange(
            @Param("locationId") UUID locationId,
            @Param("eventType") AccessEventType eventType,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
