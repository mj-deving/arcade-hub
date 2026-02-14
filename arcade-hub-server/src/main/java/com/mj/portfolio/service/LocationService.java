package com.mj.portfolio.service;

import com.mj.portfolio.dto.LocationRequest;
import com.mj.portfolio.dto.LocationResponse;
import com.mj.portfolio.entity.Location;
import com.mj.portfolio.exception.LocationNotFoundException;
import com.mj.portfolio.repository.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class LocationService {

    private final LocationRepository locationRepo;

    public LocationService(LocationRepository locationRepo) {
        this.locationRepo = locationRepo;
    }

    @Transactional(readOnly = true)
    public Page<LocationResponse> findAll(Pageable pageable) {
        return locationRepo.findAll(pageable).map(LocationResponse::from);
    }

    @Transactional(readOnly = true)
    public LocationResponse findById(UUID id) {
        return LocationResponse.from(getOrThrow(id));
    }

    public LocationResponse create(LocationRequest req) {
        Location loc = new Location();
        applyRequest(loc, req);
        return LocationResponse.from(locationRepo.save(loc));
    }

    public LocationResponse update(UUID id, LocationRequest req) {
        Location loc = getOrThrow(id);
        applyRequest(loc, req);
        return LocationResponse.from(locationRepo.save(loc));
    }

    public void delete(UUID id) {
        locationRepo.delete(getOrThrow(id));
    }

    public Location getOrThrow(UUID id) {
        return locationRepo.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(id));
    }

    private void applyRequest(Location loc, LocationRequest req) {
        loc.setName(req.getName());
        loc.setAddress(req.getAddress());
        loc.setMaxCapacity(req.getMaxCapacity() > 0 ? req.getMaxCapacity() : 100);
    }
}
