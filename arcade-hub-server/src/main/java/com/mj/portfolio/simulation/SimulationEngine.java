package com.mj.portfolio.simulation;

import com.mj.portfolio.dto.MachineEventRequest;
import com.mj.portfolio.entity.ArcadeMachine;
import com.mj.portfolio.entity.enums.MachineEventType;
import com.mj.portfolio.entity.enums.MachineStatus;
import com.mj.portfolio.repository.ArcadeMachineRepository;
import com.mj.portfolio.service.MachineEventService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

/**
 * Internal server-side simulator. Activated only when simulation.enabled=true.
 * Generates weighted random arcade machine events at a configurable interval.
 *
 * Event weights:
 *   COIN_IN      70% - regular play revenue
 *   COIN_OUT     15% - payouts
 *   MAINTENANCE  10% - routine maintenance signal
 *   ERROR         5% - fault detected
 */
@Component
@EnableScheduling
@ConditionalOnProperty(name = "simulation.enabled", havingValue = "true", matchIfMissing = false)
public class SimulationEngine {

    private final ArcadeMachineRepository machineRepo;
    private final MachineEventService machineEventService;
    private final Random random = new Random();

    public SimulationEngine(ArcadeMachineRepository machineRepo,
                             MachineEventService machineEventService) {
        this.machineRepo = machineRepo;
        this.machineEventService = machineEventService;
    }

    @Scheduled(fixedDelayString = "${simulation.interval-ms:3000}")
    public void tick() {
        List<ArcadeMachine> onlineMachines = machineRepo.findByStatus(MachineStatus.ONLINE);
        if (onlineMachines.isEmpty()) return;

        ArcadeMachine machine = onlineMachines.get(random.nextInt(onlineMachines.size()));

        MachineEventRequest req = new MachineEventRequest();
        req.setMachineId(machine.getId());
        req.setEventType(weightedEventType());
        req.setValue(randomValue(req.getEventType()));

        machineEventService.record(req);
    }

    private MachineEventType weightedEventType() {
        int roll = random.nextInt(100);
        if (roll < 70) return MachineEventType.COIN_IN;
        if (roll < 85) return MachineEventType.COIN_OUT;
        if (roll < 95) return MachineEventType.MAINTENANCE;
        return MachineEventType.ERROR;
    }

    private BigDecimal randomValue(MachineEventType type) {
        return switch (type) {
            case COIN_IN    -> BigDecimal.valueOf(0.25 + random.nextDouble() * 9.75)
                                         .setScale(2, RoundingMode.HALF_UP);
            case COIN_OUT   -> BigDecimal.valueOf(0.50 + random.nextDouble() * 49.50)
                                         .setScale(2, RoundingMode.HALF_UP);
            default         -> null;
        };
    }
}
