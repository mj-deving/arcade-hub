package com.mj.portfolio.repository;

import com.mj.portfolio.entity.ArcadeMachine;
import com.mj.portfolio.entity.enums.MachineStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ArcadeMachineRepository extends JpaRepository<ArcadeMachine, UUID> {

    Page<ArcadeMachine> findByStatus(MachineStatus status, Pageable pageable);

    List<ArcadeMachine> findByStatus(MachineStatus status);

    long countByStatus(MachineStatus status);
}
