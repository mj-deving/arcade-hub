package com.mj.portfolio.exception;

import java.util.UUID;

public class LocationCapacityExceededException extends RuntimeException {
    public LocationCapacityExceededException(UUID locationId, int maxCapacity) {
        super("Location " + locationId + " is at full capacity (" + maxCapacity + ")");
    }
}
