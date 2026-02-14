package com.mj.portfolio.service;

import com.mj.portfolio.dto.AccessEventRequest;
import com.mj.portfolio.dto.AccessEventResponse;
import com.mj.portfolio.entity.Location;
import com.mj.portfolio.entity.enums.AccessEventType;
import com.mj.portfolio.exception.LocationCapacityExceededException;
import com.mj.portfolio.repository.AccessEventRepository;
import com.mj.portfolio.repository.LocationRepository;
import com.mj.portfolio.websocket.EventBroadcaster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Plain unit test - no Spring context. Tests business logic of AccessControlService
 * in isolation using Mockito mocks.
 */
class AccessControlServiceTest {

    private AccessControlService service;
    private AccessEventRepository accessEventRepo;
    private LocationRepository locationRepo;
    private LocationService locationService;
    private EventBroadcaster broadcaster;

    @BeforeEach
    void setUp() {
        accessEventRepo = mock(AccessEventRepository.class);
        locationRepo = mock(LocationRepository.class);
        locationService = mock(LocationService.class);
        broadcaster = mock(EventBroadcaster.class);
        service = new AccessControlService(accessEventRepo, locationRepo, locationService, broadcaster);
    }

    @Test
    void checkIn_incrementsOccupancy() {
        Location loc = locationWithOccupancy(10, 50);
        UUID locationId = UUID.randomUUID();

        when(locationService.getOrThrow(locationId)).thenReturn(loc);
        when(accessEventRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AccessEventRequest req = new AccessEventRequest();
        req.setLocationId(locationId);
        req.setEventType(AccessEventType.CHECK_IN);

        service.recordEvent(req);

        assertEquals(11, loc.getCurrentOccupancy());
        verify(locationRepo).save(loc);
    }

    @Test
    void checkIn_atCapacity_throwsConflict() {
        Location loc = locationWithOccupancy(50, 50);
        UUID locationId = UUID.randomUUID();

        when(locationService.getOrThrow(locationId)).thenReturn(loc);

        AccessEventRequest req = new AccessEventRequest();
        req.setLocationId(locationId);
        req.setEventType(AccessEventType.CHECK_IN);

        assertThrows(LocationCapacityExceededException.class, () -> service.recordEvent(req));
        verify(locationRepo, never()).save(any());
    }

    @Test
    void checkOut_atZero_staysAtZero() {
        Location loc = locationWithOccupancy(0, 50);
        UUID locationId = UUID.randomUUID();

        when(locationService.getOrThrow(locationId)).thenReturn(loc);
        when(accessEventRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AccessEventRequest req = new AccessEventRequest();
        req.setLocationId(locationId);
        req.setEventType(AccessEventType.CHECK_OUT);

        service.recordEvent(req);

        assertEquals(0, loc.getCurrentOccupancy());
    }

    private Location locationWithOccupancy(int current, int max) {
        Location loc = new Location();
        loc.setName("Test Location");
        loc.setMaxCapacity(max);
        loc.setCurrentOccupancy(current);
        return loc;
    }
}
