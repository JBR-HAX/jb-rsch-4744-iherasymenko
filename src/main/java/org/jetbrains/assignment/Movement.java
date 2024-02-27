package org.jetbrains.assignment;

import java.util.Objects;

public record Movement(Direction direction, int steps) {
    public Movement {
        Objects.requireNonNull(direction, "direction");
        if (steps < 0) {
            throw new IllegalArgumentException("steps should be > 0, given: " + steps);
        }
    }
}
