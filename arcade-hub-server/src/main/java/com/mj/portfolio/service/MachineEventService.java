package com.mj.portfolio.service;

import com.mj.portfolio.dto.MachineEventRequest;
import com.mj.portfolio.dto.MachineEventResponse;
import com.mj.portfolio.dto.WebSocketEvent;
import com.mj.portfolio.entity.ArcadeMachine;
import com.mj.portfolio.entity.MachineEvent;
import com.mj.portfolio.repository.MachineEventRepository;
import com.mj.portfolio.websocket.EventBroadcaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class MachineEventService {

    private final MachineEventRepository eventRepo;
    private final ArcadeMachineService machineService;
    private final EventBroadcaster broadcaster;

    public MachineEventService(MachineEventRepository eventRepo,
                                ArcadeMachineService machineService,
                                EventBroadcaster broadcaster) {
        this.eventRepo = eventRepo;
        this.machineService = machineService;
        this.broadcaster = broadcaster;
    }

    @Transactional(readOnly = true)
    public Page<MachineEventResponse> findAll(UUID machineId, Pageable pageable) {
        Page<MachineEvent> page = (machineId != null)
                ? eventRepo.findByMachineId(machineId, pageable)
                : eventRepo.findAll(pageable);
        return page.map(MachineEventResponse::from);
    }

    public MachineEventResponse record(MachineEventRequest req) {
        ArcadeMachine machine = machineService.getOrThrow(req.getMachineId());

        MachineEvent event = new MachineEvent();
        event.setMachine(machine);
        event.setEventType(req.getEventType());
        event.setValue(req.getValue());

        MachineEvent saved = eventRepo.save(event);

        // Broadcast to all WebSocket subscribers after persisting
        WebSocketEvent wsEvent = new WebSocketEvent();
        wsEvent.setType("MACHINE_EVENT");
        wsEvent.setMachineId(machine.getId());
        wsEvent.setMachineName(machine.getName());
        wsEvent.setEventType(saved.getEventType().name());
        wsEvent.setValue(saved.getValue());
        wsEvent.setTimestamp(saved.getTimestamp());
        if (machine.getLocation() != null) {
            wsEvent.setLocationId(machine.getLocation().getId());
        }
        broadcaster.broadcast(wsEvent);

        return MachineEventResponse.from(saved);
    }
}
