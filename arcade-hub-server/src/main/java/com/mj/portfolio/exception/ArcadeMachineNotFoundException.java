package com.mj.portfolio.exception;

import java.util.UUID;

public class ArcadeMachineNotFoundException extends RuntimeException {
    public ArcadeMachineNotFoundException(UUID id) {
        super("Arcade machine not found: " + id);
    }
}
