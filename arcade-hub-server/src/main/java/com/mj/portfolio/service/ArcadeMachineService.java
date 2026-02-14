package com.mj.portfolio.service;

import com.mj.portfolio.dto.ArcadeMachineRequest;
import com.mj.portfolio.dto.ArcadeMachineResponse;
import com.mj.portfolio.entity.ArcadeMachine;
import com.mj.portfolio.entity.Location;
import com.mj.portfolio.entity.enums.MachineStatus;
import com.mj.portfolio.exception.ArcadeMachineNotFoundException;
import com.mj.portfolio.repository.ArcadeMachineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class ArcadeMachineService {

    private final ArcadeMachineRepository machineRepo;
    private final LocationService locationService;

    public ArcadeMachineService(ArcadeMachineRepository machineRepo,
                                 LocationService locationService) {
        this.machineRepo = machineRepo;
        this.locationService = locationService;
    }

    @Transactional(readOnly = true)
    public Page<ArcadeMachineResponse> findAll(MachineStatus status, Pageable pageable) {
        Page<ArcadeMachine> page = (status != null)
                ? machineRepo.findByStatus(status, pageable)
                : machineRepo.findAll(pageable);
        return page.map(ArcadeMachineResponse::from);
    }

    @Transactional(readOnly = true)
    public ArcadeMachineResponse findById(UUID id) {
        return ArcadeMachineResponse.from(getOrThrow(id));
    }

    public ArcadeMachineResponse create(ArcadeMachineRequest req) {
        ArcadeMachine machine = new ArcadeMachine();
        applyRequest(machine, req);
        return ArcadeMachineResponse.from(machineRepo.save(machine));
    }

    public ArcadeMachineResponse update(UUID id, ArcadeMachineRequest req) {
        ArcadeMachine machine = getOrThrow(id);
        applyRequest(machine, req);
        return ArcadeMachineResponse.from(machineRepo.save(machine));
    }

    public ArcadeMachineResponse heartbeat(UUID id) {
        ArcadeMachine machine = getOrThrow(id);
        machine.setStatus(MachineStatus.ONLINE);
        machine.setLastHeartbeat(LocalDateTime.now());
        return ArcadeMachineResponse.from(machineRepo.save(machine));
    }

    public void delete(UUID id) {
        machineRepo.delete(getOrThrow(id));
    }

    public ArcadeMachine getOrThrow(UUID id) {
        return machineRepo.findById(id)
                .orElseThrow(() -> new ArcadeMachineNotFoundException(id));
    }

    private void applyRequest(ArcadeMachine machine, ArcadeMachineRequest req) {
        machine.setName(req.getName());
        machine.setType(req.getType());
        machine.setStatus(req.getStatus() != null ? req.getStatus() : MachineStatus.OFFLINE);
        if (req.getLocationId() != null) {
            Location loc = locationService.getOrThrow(req.getLocationId());
            machine.setLocation(loc);
        } else {
            machine.setLocation(null);
        }
    }
}
