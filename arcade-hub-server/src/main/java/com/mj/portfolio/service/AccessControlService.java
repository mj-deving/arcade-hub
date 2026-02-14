package com.mj.portfolio.service;

import com.mj.portfolio.dto.AccessEventRequest;
import com.mj.portfolio.dto.AccessEventResponse;
import com.mj.portfolio.dto.WebSocketEvent;
import com.mj.portfolio.entity.AccessEvent;
import com.mj.portfolio.entity.Location;
import com.mj.portfolio.entity.enums.AccessEventType;
import com.mj.portfolio.exception.LocationCapacityExceededException;
import com.mj.portfolio.repository.AccessEventRepository;
import com.mj.portfolio.repository.LocationRepository;
import com.mj.portfolio.websocket.EventBroadcaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AccessControlService {

    private final AccessEventRepository accessEventRepo;
    private final LocationRepository locationRepo;
    private final LocationService locationService;
    private final EventBroadcaster broadcaster;

    public AccessControlService(AccessEventRepository accessEventRepo,
                                 LocationRepository locationRepo,
                                 LocationService locationService,
                                 EventBroadcaster broadcaster) {
        this.accessEventRepo = accessEventRepo;
        this.locationRepo = locationRepo;
        this.locationService = locationService;
        this.broadcaster = broadcaster;
    }

    @Transactional(readOnly = true)
    public Page<AccessEventResponse> findAll(UUID locationId, Pageable pageable) {
        Page<AccessEvent> page = (locationId != null)
                ? accessEventRepo.findByLocationId(locationId, pageable)
                : accessEventRepo.findAll(pageable);
        return page.map(AccessEventResponse::from);
    }

    /**
     * Records a CHECK_IN or CHECK_OUT event, updating location occupancy atomically.
     * CHECK_IN throws 409 if location is at max capacity.
     * CHECK_OUT clamps at 0 to prevent negative occupancy.
     */
    public AccessEventResponse recordEvent(AccessEventRequest req) {
        Location location = locationService.getOrThrow(req.getLocationId());

        if (req.getEventType() == AccessEventType.CHECK_IN) {
            if (location.getCurrentOccupancy() >= location.getMaxCapacity()) {
                throw new LocationCapacityExceededException(location.getId(), location.getMaxCapacity());
            }
            location.setCurrentOccupancy(location.getCurrentOccupancy() + 1);
        } else {
            location.setCurrentOccupancy(Math.max(0, location.getCurrentOccupancy() - 1));
        }
        locationRepo.save(location);

        AccessEvent event = new AccessEvent();
        event.setLocation(location);
        event.setPersonId(req.getPersonId());
        event.setEventType(req.getEventType());
        event.setTimestamp(LocalDateTime.now());
        AccessEvent saved = accessEventRepo.save(event);

        // Broadcast access event over WebSocket
        WebSocketEvent wsEvent = new WebSocketEvent();
        wsEvent.setType("ACCESS_EVENT");
        wsEvent.setLocationId(location.getId());
        wsEvent.setEventType(req.getEventType().name());
        wsEvent.setTimestamp(saved.getTimestamp());
        broadcaster.broadcast(wsEvent);

        return AccessEventResponse.from(saved);
    }
}
